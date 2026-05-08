package com.team.prezel.core.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.math.max

internal class MediaRecordingAudioController @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : RecordingAudioController {
    private val controllerScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _audioSessionState = MutableStateFlow<AudioSessionState>(AudioSessionState.Idle)
    override val audioSessionState: StateFlow<AudioSessionState> = _audioSessionState.asStateFlow()

    private val _audioSessionEffect = Channel<AudioSessionEffect>(capacity = Channel.BUFFERED)
    override val audioSessionEffect: Flow<AudioSessionEffect> = _audioSessionEffect.receiveAsFlow()

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var currentAudioFile: File? = null
    private var recordingTimerJob: Job? = null
    private var playbackTimerJob: Job? = null

    override fun startRecording() {
        runCatching {
            stopPlayback()
            releaseRecorder()
            deleteCurrentAudioFile()

            val file = File.createTempFile("recording_", ".m4a", context.cacheDir)
            var pendingRecorder: MediaRecorder? = null
            val newRecorder = runCatching {
                val recorder = createMediaRecorder(context = context)
                pendingRecorder = recorder
                recorder.apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setOutputFile(file.absolutePath)
                    prepare()
                    start()
                }
            }.getOrElse { throwable ->
                pendingRecorder?.release()
                file.delete()
                throw throwable
            }

            recorder = newRecorder
            currentAudioFile = file
            _audioSessionState.value = AudioSessionState.Recording(elapsedSeconds = 0)
            startRecordingTimer()
        }.onFailure {
            releaseRecorder()
            deleteCurrentAudioFile()
            _audioSessionState.value = AudioSessionState.Idle
            emitEffect(AudioSessionEffect.RecordingStartFailed)
        }
    }

    override fun stopRecording() {
        val state = audioSessionState.value
        val elapsedSeconds = when (state) {
            is AudioSessionState.Recording -> state.elapsedSeconds
            else -> return
        }
        val file = currentAudioFile ?: return emitEffect(AudioSessionEffect.RecordingStopFailed)

        recordingTimerJob?.cancel()
        runCatching {
            recorder?.stop() ?: error("Recording is not active.")
            max(elapsedSeconds, 0)
        }.onSuccess { durationSeconds ->
            releaseRecorder()
            _audioSessionState.value = AudioSessionState.ReadyToPlay(
                source = AudioSource.RecordedFile(filePath = file.absolutePath),
                durationSeconds = durationSeconds,
            )
        }.onFailure {
            releaseRecorder()
            deleteCurrentAudioFile()
            _audioSessionState.value = AudioSessionState.Idle
            emitEffect(AudioSessionEffect.RecordingStopFailed)
        }
    }

    override fun startPlayback() {
        when (val state = audioSessionState.value) {
            is AudioSessionState.ReadyToPlay -> startPlayback(
                source = state.source,
                durationSeconds = state.durationSeconds,
                startPositionSeconds = state.positionSeconds.takeIf { it < state.durationSeconds } ?: 0,
            )

            else -> Unit
        }
    }

    override fun stopPlayback() {
        val readyState = when (val state = audioSessionState.value) {
            is AudioSessionState.Playing -> AudioSessionState.ReadyToPlay(
                source = state.source,
                positionSeconds = playbackPositionSeconds().coerceAtLeast(state.positionSeconds),
                durationSeconds = state.durationSeconds,
            )

            else -> null
        }

        releasePlayer()
        if (readyState != null) {
            _audioSessionState.value = readyState
        }
    }

    override fun reset() {
        recordingTimerJob?.cancel()
        playbackTimerJob?.cancel()
        releaseRecorder()
        releasePlayer()
        deleteCurrentAudioFile()
        _audioSessionState.value = AudioSessionState.Idle
    }

    override fun release() {
        reset()
        controllerScope.cancel()
    }

    private fun startPlayback(
        source: AudioSource,
        durationSeconds: Int,
        startPositionSeconds: Int,
    ) {
        runCatching {
            releasePlayer()

            var pendingPlayer: MediaPlayer? = null
            val newPlayer = runCatching {
                val mediaPlayer = MediaPlayer()
                pendingPlayer = mediaPlayer
                mediaPlayer.apply {
                    setDataSource(source.filePath)
                    prepare()
                    if (startPositionSeconds > 0) {
                        seekTo(startPositionSeconds * MILLIS_PER_SECOND)
                    }
                    setOnCompletionListener {
                        releasePlayer()
                        _audioSessionState.value = AudioSessionState.ReadyToPlay(
                            source = source,
                            positionSeconds = durationSeconds,
                            durationSeconds = durationSeconds,
                        )
                    }
                    start()
                }
            }.getOrElse { throwable ->
                pendingPlayer?.release()
                throw throwable
            }

            player = newPlayer
            _audioSessionState.value = AudioSessionState.Playing(
                source = source,
                positionSeconds = startPositionSeconds,
                durationSeconds = durationSeconds.coerceAtLeast(newPlayer.duration.toSeconds()),
            )
            startPlaybackTimer()
        }.onFailure {
            releasePlayer()
            _audioSessionState.value = AudioSessionState.ReadyToPlay(
                source = source,
                durationSeconds = durationSeconds,
            )
            emitEffect(AudioSessionEffect.PlaybackStartFailed)
        }
    }

    private fun startRecordingTimer() {
        recordingTimerJob?.cancel()
        recordingTimerJob = controllerScope.launch {
            while (true) {
                delay(RECORDING_TIMER_DELAY_MILLIS)
                _audioSessionState.update { state ->
                    if (state !is AudioSessionState.Recording) return@update state
                    AudioSessionState.Recording(elapsedSeconds = state.elapsedSeconds + 1)
                }
            }
        }
    }

    private fun startPlaybackTimer() {
        playbackTimerJob?.cancel()
        playbackTimerJob = controllerScope.launch {
            while (true) {
                delay(PLAYBACK_TIMER_DELAY_MILLIS)
                _audioSessionState.update { state ->
                    if (state !is AudioSessionState.Playing) return@update state
                    AudioSessionState.Playing(
                        source = state.source,
                        positionSeconds = playbackPositionSeconds(),
                        durationSeconds = state.durationSeconds,
                    )
                }
            }
        }
    }

    private fun releaseRecorder() {
        recorder?.release()
        recorder = null
    }

    private fun releasePlayer() {
        playbackTimerJob?.cancel()
        player?.runCatching { stop() }
        player?.release()
        player = null
    }

    private fun playbackPositionSeconds(): Int = runCatching { player?.currentPosition?.toSeconds() }.getOrNull() ?: 0

    private fun deleteCurrentAudioFile() {
        currentAudioFile?.delete()
        currentAudioFile = null
    }

    private fun emitEffect(effect: AudioSessionEffect) {
        _audioSessionEffect.trySend(effect)
    }

    private companion object {
        const val MILLIS_PER_SECOND = 1_000
        const val RECORDING_TIMER_DELAY_MILLIS = 1_000L
        const val PLAYBACK_TIMER_DELAY_MILLIS = 250L
    }
}

@Suppress("DEPRECATION")
private fun createMediaRecorder(context: Context): MediaRecorder =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        MediaRecorder(context)
    } else {
        MediaRecorder()
    }

private fun Int.toSeconds(): Int = this / 1_000
