package com.team.prezel.core.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _audioSessionEvent = MutableSharedFlow<AudioSessionEvent>(extraBufferCapacity = EVENT_BUFFER_CAPACITY)
    override val audioSessionEvent: SharedFlow<AudioSessionEvent> = _audioSessionEvent.asSharedFlow()

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
                val recorder = createMediaRecorder()
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
            emitEvent(AudioSessionEvent.RecordingStartFailed)
        }
    }

    override fun pauseRecording() {
        val state = audioSessionState.value as? AudioSessionState.Recording ?: return

        runCatching {
            recorder?.pause() ?: error("Recording is not active.")
        }.onSuccess {
            recordingTimerJob?.cancel()
            _audioSessionState.value = AudioSessionState.RecordingPaused(
                elapsedSeconds = state.elapsedSeconds,
            )
        }.onFailure {
            emitEvent(AudioSessionEvent.RecordingPauseFailed)
        }
    }

    override fun resumeRecording() {
        val state = audioSessionState.value as? AudioSessionState.RecordingPaused ?: return

        runCatching {
            recorder?.resume() ?: error("Recording is not active.")
        }.onSuccess {
            _audioSessionState.value = AudioSessionState.Recording(
                elapsedSeconds = state.elapsedSeconds,
            )
            startRecordingTimer()
        }.onFailure {
            emitEvent(AudioSessionEvent.RecordingResumeFailed)
        }
    }

    override fun stopRecording() {
        val state = audioSessionState.value
        val elapsedSeconds = when (state) {
            is AudioSessionState.Recording -> state.elapsedSeconds
            is AudioSessionState.RecordingPaused -> state.elapsedSeconds
            else -> return
        }
        val file = currentAudioFile ?: return emitEvent(AudioSessionEvent.RecordingStopFailed)

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
            emitEvent(AudioSessionEvent.RecordingStopFailed)
        }
    }

    override fun resetRecording() {
        when (audioSessionState.value) {
            is AudioSessionState.Recording,
            is AudioSessionState.RecordingPaused,
                -> {
                recordingTimerJob?.cancel()
                releaseRecorder()
                deleteCurrentAudioFile()
                _audioSessionState.value = AudioSessionState.Idle
            }

            else -> Unit
        }
    }

    override fun loadAudioFile(uri: Uri) {
        runCatching {
            stopPlayback()
            releaseRecorder()
            deleteCurrentAudioFile()

            val file = File.createTempFile("external_audio_", ".m4a", context.cacheDir)
            context.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            } ?: error("Audio file could not be opened.")

            val durationSeconds = readDurationSeconds(file)
            currentAudioFile = file
            _audioSessionState.value = AudioSessionState.ReadyToPlay(
                source = AudioSource.ExternalFile(filePath = file.absolutePath),
                durationSeconds = durationSeconds,
            )
        }.onFailure {
            deleteCurrentAudioFile()
            _audioSessionState.value = AudioSessionState.Idle
            emitEvent(AudioSessionEvent.FileLoadFailed)
        }
    }

    override fun startPlayback() {
        when (val state = audioSessionState.value) {
            is AudioSessionState.ReadyToPlay -> startPlayback(
                source = state.source,
                durationSeconds = state.durationSeconds,
                startPositionSeconds = 0,
            )

            is AudioSessionState.PlaybackPaused -> startPlayback(
                source = state.source,
                durationSeconds = state.durationSeconds,
                startPositionSeconds = state.positionSeconds,
            )

            else -> Unit
        }
    }

    override fun pausePlayback() {
        val state = audioSessionState.value as? AudioSessionState.Playing ?: return

        runCatching {
            player?.pause() ?: error("Playback is not active.")
        }.onSuccess {
            playbackTimerJob?.cancel()
            _audioSessionState.value = AudioSessionState.PlaybackPaused(
                source = state.source,
                positionSeconds = playbackPositionSeconds().coerceAtLeast(state.positionSeconds),
                durationSeconds = state.durationSeconds,
            )
        }
    }

    override fun resumePlayback() {
        val state = audioSessionState.value as? AudioSessionState.PlaybackPaused ?: return

        runCatching {
            player?.start() ?: error("Playback is not active.")
        }.onSuccess {
            _audioSessionState.value = AudioSessionState.Playing(
                source = state.source,
                positionSeconds = state.positionSeconds,
                durationSeconds = state.durationSeconds,
            )
            startPlaybackTimer()
        }.onFailure {
            emitEvent(AudioSessionEvent.PlaybackStartFailed)
        }
    }

    override fun stopPlayback() {
        val readyState = when (val state = audioSessionState.value) {
            is AudioSessionState.Playing -> AudioSessionState.ReadyToPlay(
                source = state.source,
                durationSeconds = state.durationSeconds,
            )

            is AudioSessionState.PlaybackPaused -> AudioSessionState.ReadyToPlay(
                source = state.source,
                durationSeconds = state.durationSeconds,
            )

            else -> null
        }

        releasePlayer()
        if (readyState != null) {
            _audioSessionState.value = readyState
        }
    }

    override fun release() {
        recordingTimerJob?.cancel()
        playbackTimerJob?.cancel()
        releaseRecorder()
        releasePlayer()
        deleteCurrentAudioFile()
        controllerScope.cancel()
        _audioSessionState.value = AudioSessionState.Idle
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
            emitEvent(AudioSessionEvent.PlaybackStartFailed)
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

    private fun readDurationSeconds(file: File): Int =
        runCatching {
            val mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(file.absolutePath)
                mediaPlayer.prepare()
                mediaPlayer.duration.toSeconds()
            } finally {
                mediaPlayer.release()
            }
        }.getOrDefault(0)

    @Suppress("DEPRECATION")
    private fun createMediaRecorder(): MediaRecorder =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
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

    private fun emitEvent(event: AudioSessionEvent) {
        _audioSessionEvent.tryEmit(event)
    }

    private companion object {
        const val EVENT_BUFFER_CAPACITY = 8
        const val MILLIS_PER_SECOND = 1_000
        const val RECORDING_TIMER_DELAY_MILLIS = 1_000L
        const val PLAYBACK_TIMER_DELAY_MILLIS = 250L
    }
}

private fun Int.toSeconds(): Int = this / 1_000
