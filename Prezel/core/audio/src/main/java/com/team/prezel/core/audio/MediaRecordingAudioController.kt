package com.team.prezel.core.audio

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

internal class MediaRecordingAudioController @Inject constructor(
    private val recorderSession: AudioRecorderSession,
    private val playerSession: AudioPlayerSession,
) : RecordingAudioController {
    private val controllerScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _audioSessionState = MutableStateFlow<AudioSessionState>(AudioSessionState.Idle)
    override val audioSessionState: StateFlow<AudioSessionState> = _audioSessionState.asStateFlow()

    private val _audioSessionEffect = Channel<AudioSessionEffect>(capacity = Channel.BUFFERED)
    override val audioSessionEffect: Flow<AudioSessionEffect> = _audioSessionEffect.receiveAsFlow()

    private var recordingTimerJob: Job? = null
    private var playbackTimerJob: Job? = null

    override fun startRecording() {
        runCatching {
            stopPlayback()
            recorderSession.start().getOrThrow()
            _audioSessionState.value = AudioSessionState.Recording(elapsedSeconds = 0)
            startRecordingTimer()
        }.onFailure {
            recorderSession.reset()
            _audioSessionState.value = AudioSessionState.Idle
            emitEffect(AudioSessionEffect.RecordingStartFailed)
        }
    }

    override fun stopRecording() {
        val elapsedSeconds = when (val state = audioSessionState.value) {
            is AudioSessionState.Recording -> state.elapsedSeconds
            else -> return
        }

        recordingTimerJob?.cancel()
        recorderSession
            .stop(elapsedSeconds = elapsedSeconds)
            .onSuccess { recordedAudio ->
                _audioSessionState.value = AudioSessionState.ReadyToPlay(
                    source = recordedAudio.source,
                    durationSeconds = recordedAudio.durationSeconds,
                )
            }.onFailure {
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
        playbackTimerJob?.cancel()
        recorderSession.reset()
        releasePlayer()
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
                startPlaybackTimer()
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
        emitEffect(AudioSessionEffect.PlaybackStartFailed)
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
                        positionSeconds = playerSession.currentPositionSeconds(),
                        durationSeconds = state.durationSeconds,
                    )
                }
            }
        }
    }

    private fun releasePlayer() {
        playbackTimerJob?.cancel()
        playerSession.release()
    }

    private fun emitEffect(effect: AudioSessionEffect) {
        _audioSessionEffect.trySend(effect)
    }

    private companion object {
        const val RECORDING_TIMER_DELAY_MILLIS = 1_000L
        const val PLAYBACK_TIMER_DELAY_MILLIS = 250L
    }
}
