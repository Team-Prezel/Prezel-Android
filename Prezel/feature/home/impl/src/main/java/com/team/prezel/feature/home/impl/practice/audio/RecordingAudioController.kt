package com.team.prezel.feature.home.impl.practice.audio

internal interface RecordingAudioController {
    fun startRecording(): Result<String>

    fun stopRecording(): Result<Int>

    fun startPlayback(
        filePath: String,
        onComplete: () -> Unit,
    ): Result<Int>

    fun stopPlayback()

    fun playbackPositionSeconds(): Int

    fun release()
}
