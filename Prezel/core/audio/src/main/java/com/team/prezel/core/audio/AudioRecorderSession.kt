package com.team.prezel.core.audio

internal interface AudioRecorderSession {
    fun start(): Result<Unit>

    fun stop(elapsedSeconds: Int): Result<RecordedAudio>

    fun reset()
}
