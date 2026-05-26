package com.team.prezel.core.audio

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RecordingAudioController {
    val audioSessionState: StateFlow<AudioSessionState>

    val recordingVolumes: StateFlow<ImmutableList<Float>>

    val audioSessionEffect: Flow<AudioSessionEffect>

    fun startRecording()

    fun pauseRecording()

    fun resumeRecording()

    fun stopRecording()

    fun startPlayback()

    fun stopPlayback()

    fun reset()

    fun release()
}
