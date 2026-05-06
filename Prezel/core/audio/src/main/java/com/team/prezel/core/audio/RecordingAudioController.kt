package com.team.prezel.core.audio

import android.net.Uri
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface RecordingAudioController {
    val audioSessionState: StateFlow<AudioSessionState>

    val audioSessionEvent: SharedFlow<AudioSessionEvent>

    fun startRecording()

    fun pauseRecording()

    fun resumeRecording()

    fun stopRecording()

    fun resetRecording()

    fun loadAudioFile(uri: Uri)

    fun startPlayback()

    fun pausePlayback()

    fun resumePlayback()

    fun stopPlayback()

    fun release()
}

sealed interface AudioSessionState {
    data object Idle : AudioSessionState

    data class Recording(
        val elapsedSeconds: Int,
    ) : AudioSessionState

    data class RecordingPaused(
        val elapsedSeconds: Int,
    ) : AudioSessionState

    data class ReadyToPlay(
        val source: AudioSource,
        val durationSeconds: Int,
    ) : AudioSessionState

    data class Playing(
        val source: AudioSource,
        val positionSeconds: Int,
        val durationSeconds: Int,
    ) : AudioSessionState

    data class PlaybackPaused(
        val source: AudioSource,
        val positionSeconds: Int,
        val durationSeconds: Int,
    ) : AudioSessionState
}

sealed interface AudioSource {
    val filePath: String

    data class RecordedFile(
        override val filePath: String,
    ) : AudioSource

    data class ExternalFile(
        override val filePath: String,
    ) : AudioSource
}

sealed interface AudioSessionEvent {
    data object RecordingStartFailed : AudioSessionEvent

    data object RecordingPauseFailed : AudioSessionEvent

    data object RecordingResumeFailed : AudioSessionEvent

    data object RecordingStopFailed : AudioSessionEvent

    data object PlaybackStartFailed : AudioSessionEvent

    data object FileLoadFailed : AudioSessionEvent
}
