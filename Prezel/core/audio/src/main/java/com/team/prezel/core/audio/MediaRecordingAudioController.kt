package com.team.prezel.core.audio

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
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
import javax.inject.Inject

// Android MediaRecorder의 maxAmplitude는 0..32767 범위로 전달된다.
private const val MAX_RECORDING_AMPLITUDE = 32_767f

// 화면에 표시되는 녹음 시간은 초 단위라서 1초마다 갱신한다.
private const val RECORDING_TIMER_DELAY_MILLIS = 1_000L

// 파형이 부드럽게 반응하도록 초당 20번 입력 볼륨을 샘플링한다.
private const val RECORDING_VOLUME_DELAY_MILLIS = 50L

// 재생 시간 상태는 초 단위로 갱신하고, 파형 진행은 UI에서 보간한다.
private const val PLAYBACK_TIMER_DELAY_MILLIS = 1_000L

internal class MediaRecordingAudioController @Inject constructor(
    private val recorderSession: AudioRecorderSession,
    private val playerSession: AudioPlayerSession,
) : RecordingAudioController {
    private val controllerScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _audioSessionState = MutableStateFlow<AudioSessionState>(AudioSessionState.Idle)
    override val audioSessionState: StateFlow<AudioSessionState> = _audioSessionState.asStateFlow()

    private val _recordingVolumes = MutableStateFlow<ImmutableList<Float>>(persistentListOf())
    override val recordingVolumes: StateFlow<ImmutableList<Float>> = _recordingVolumes.asStateFlow()

    private val _audioSessionEffect = Channel<AudioSessionEffect>(capacity = Channel.BUFFERED)
    override val audioSessionEffect: Flow<AudioSessionEffect> = _audioSessionEffect.receiveAsFlow()

    private var recordingTimerJob: Job? = null
    private var recordingVolumeJob: Job? = null
    private var playbackTimerJob: Job? = null

    override fun startRecording() {
        runCatching {
            stopPlayback()
            recorderSession.start().getOrThrow()
            _recordingVolumes.value = persistentListOf()
            _audioSessionState.value = AudioSessionState.Recording(elapsedSeconds = 0)
            recordingTimerJob?.cancel()
            recordingTimerJob = controllerScope.launchRecordingTimer(_audioSessionState)
            recordingVolumeJob?.cancel()
            recordingVolumeJob = controllerScope.launchRecordingVolumeMeter(
                audioSessionState = audioSessionState,
                recorderSession = recorderSession,
                recordingVolumes = _recordingVolumes,
            )
        }.onFailure {
            recorderSession.reset()
            recordingVolumeJob?.cancel()
            _recordingVolumes.value = persistentListOf()
            _audioSessionState.value = AudioSessionState.Idle
            _audioSessionEffect.emit(AudioSessionEffect.RecordingStartFailed)
        }
    }

    override fun stopRecording() {
        val elapsedSeconds = when (val state = audioSessionState.value) {
            is AudioSessionState.Recording -> state.elapsedSeconds
            is AudioSessionState.PausedRecording -> state.elapsedSeconds
            else -> return
        }

        recordingTimerJob?.cancel()
        recordingVolumeJob?.cancel()
        recorderSession
            .stop(elapsedSeconds = elapsedSeconds)
            .onSuccess { recordedAudio ->
                _audioSessionState.value = AudioSessionState.ReadyToPlay(
                    source = recordedAudio.source,
                    durationSeconds = recordedAudio.durationSeconds,
                )
            }.onFailure {
                _recordingVolumes.value = persistentListOf()
                _audioSessionState.value = AudioSessionState.Idle
                _audioSessionEffect.emit(AudioSessionEffect.RecordingStopFailed)
            }
    }

    override fun pauseRecording() {
        val elapsedSeconds = when (val state = audioSessionState.value) {
            is AudioSessionState.Recording -> state.elapsedSeconds
            else -> return
        }

        recorderSession
            .pause()
            .onSuccess {
                recordingTimerJob?.cancel()
                recordingVolumeJob?.cancel()
                _audioSessionState.value = AudioSessionState.PausedRecording(elapsedSeconds = elapsedSeconds)
            }.onFailure {
                _audioSessionEffect.emit(AudioSessionEffect.RecordingStopFailed)
            }
    }

    override fun resumeRecording() {
        val elapsedSeconds = when (val state = audioSessionState.value) {
            is AudioSessionState.PausedRecording -> state.elapsedSeconds
            else -> return
        }

        recorderSession
            .resume()
            .onSuccess {
                _audioSessionState.value = AudioSessionState.Recording(elapsedSeconds = elapsedSeconds)
                recordingTimerJob?.cancel()
                recordingTimerJob = controllerScope.launchRecordingTimer(_audioSessionState)
                recordingVolumeJob?.cancel()
                recordingVolumeJob = controllerScope.launchRecordingVolumeMeter(
                    audioSessionState = audioSessionState,
                    recorderSession = recorderSession,
                    recordingVolumes = _recordingVolumes,
                )
            }.onFailure {
                _audioSessionEffect.emit(AudioSessionEffect.RecordingStartFailed)
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
                positionSeconds = playerSession
                    .currentPositionSeconds()
                    .coerceAtLeast(state.positionSeconds),
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
        recordingVolumeJob?.cancel()
        playbackTimerJob?.cancel()
        recorderSession.reset()
        releasePlayer()
        _recordingVolumes.value = persistentListOf()
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
        playerSession
            .start(
                source = source,
                startPositionSeconds = startPositionSeconds,
                onCompleted = {
                    handlePlaybackCompleted(
                        source = source,
                        durationSeconds = durationSeconds,
                    )
                },
            ).onSuccess { playerDurationMillis ->
                _audioSessionState.value = AudioSessionState.Playing(
                    source = source,
                    positionSeconds = startPositionSeconds,
                    durationSeconds = durationSeconds.coerceAtLeast(playerDurationMillis.toSeconds()),
                )
                playbackTimerJob?.cancel()
                playbackTimerJob = controllerScope.launchPlaybackTimer(_audioSessionState)
            }.onFailure {
                handlePlaybackStartFailure(
                    source = source,
                    durationSeconds = durationSeconds,
                )
            }
    }

    private fun handlePlaybackCompleted(
        source: AudioSource,
        durationSeconds: Int,
    ) {
        releasePlayer()
        _audioSessionState.value = AudioSessionState.ReadyToPlay(
            source = source,
            positionSeconds = durationSeconds,
            durationSeconds = durationSeconds,
        )
    }

    private fun handlePlaybackStartFailure(
        source: AudioSource,
        durationSeconds: Int,
    ) {
        releasePlayer()
        _audioSessionState.value = AudioSessionState.ReadyToPlay(
            source = source,
            durationSeconds = durationSeconds,
        )
        _audioSessionEffect.emit(AudioSessionEffect.PlaybackStartFailed)
    }

    private fun releasePlayer() {
        playbackTimerJob?.cancel()
        playerSession.release()
    }
}

private fun Channel<AudioSessionEffect>.emit(effect: AudioSessionEffect) {
    trySend(effect)
}

private fun CoroutineScope.launchRecordingTimer(audioSessionState: MutableStateFlow<AudioSessionState>): Job =
    launch {
        while (true) {
            delay(RECORDING_TIMER_DELAY_MILLIS)
            audioSessionState.update { state ->
                if (state !is AudioSessionState.Recording) return@update state
                AudioSessionState.Recording(elapsedSeconds = state.elapsedSeconds + 1)
            }
        }
    }

private fun CoroutineScope.launchRecordingVolumeMeter(
    audioSessionState: StateFlow<AudioSessionState>,
    recorderSession: AudioRecorderSession,
    recordingVolumes: MutableStateFlow<ImmutableList<Float>>,
): Job =
    launch {
        while (true) {
            delay(RECORDING_VOLUME_DELAY_MILLIS)
            if (audioSessionState.value !is AudioSessionState.Recording) continue

            recordingVolumes.update { volumes ->
                (volumes + recorderSession.maxAmplitude().toVolume()).toImmutableList()
            }
        }
    }

private fun CoroutineScope.launchPlaybackTimer(audioSessionState: MutableStateFlow<AudioSessionState>): Job =
    launch {
        while (true) {
            delay(PLAYBACK_TIMER_DELAY_MILLIS)
            audioSessionState.update { state ->
                if (state !is AudioSessionState.Playing) return@update state

                AudioSessionState.Playing(
                    source = state.source,
                    positionSeconds = (state.positionSeconds + 1).coerceAtMost(state.durationSeconds),
                    durationSeconds = state.durationSeconds,
                )
            }
        }
    }

private fun Int.toVolume(): Float =
    (this / MAX_RECORDING_AMPLITUDE).coerceIn(
        minimumValue = 0.1f,
        maximumValue = 1f,
    )
