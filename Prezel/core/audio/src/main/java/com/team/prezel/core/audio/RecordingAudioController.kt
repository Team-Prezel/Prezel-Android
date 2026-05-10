package com.team.prezel.core.audio

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RecordingAudioController {
    val audioSessionState: StateFlow<AudioSessionState>

    val audioSessionEffect: Flow<AudioSessionEffect>

    fun startRecording()

    fun stopRecording()

    fun startPlayback()

    fun stopPlayback()

    fun reset()

    fun release()
}

@Immutable
sealed interface AudioSessionState {
    data object Idle : AudioSessionState

    data class Recording(
        val elapsedSeconds: Int,
    ) : AudioSessionState

    data class ReadyToPlay(
        val source: AudioSource,
        val positionSeconds: Int = 0,
        val durationSeconds: Int,
    ) : AudioSessionState

    data class Playing(
        val source: AudioSource,
        val positionSeconds: Int,
        val durationSeconds: Int,
    ) : AudioSessionState
}

@Immutable
sealed interface AudioSource {
    val filePath: String

    data class RecordedFile(
        override val filePath: String,
    ) : AudioSource
}

sealed interface AudioSessionEffect {
    data object RecordingStartFailed : AudioSessionEffect

    data object RecordingStopFailed : AudioSessionEffect

    data object PlaybackStartFailed : AudioSessionEffect
}
