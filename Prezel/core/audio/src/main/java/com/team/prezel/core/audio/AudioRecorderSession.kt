package com.team.prezel.core.audio

internal interface AudioRecorderSession {
    fun start(): Result<Unit>

    fun pause(): Result<Unit>

    fun resume(): Result<Unit>

    fun maxAmplitude(): Int

    fun stop(elapsedSeconds: Int): Result<RecordedAudio>

    fun reset()
}
