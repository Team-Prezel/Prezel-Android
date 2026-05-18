package com.team.prezel.core.audio

internal interface AudioPlayerSession {
    fun start(
        source: AudioSource,
        startPositionSeconds: Int,
        onCompleted: () -> Unit,
    ): Result<Int>

    fun currentPositionSeconds(): Int

    fun release()
}
