package com.team.prezel.core.audio

interface RecordingAudioController {
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
