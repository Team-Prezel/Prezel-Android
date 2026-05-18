package com.team.prezel.core.audio

import android.media.MediaPlayer
import javax.inject.Inject

internal class MediaPlayerSession @Inject constructor() : AudioPlayerSession {
    private var player: MediaPlayer? = null

    override fun start(
        source: AudioSource,
        startPositionSeconds: Int,
        onCompleted: () -> Unit,
    ): Result<Int> =
        runCatching {
            release()

            var pendingPlayer: MediaPlayer? = null
            val newPlayer = runCatching {
                MediaPlayer().also { pendingPlayer = it }.apply {
                    setDataSource(source.filePath)
                    prepare()
                    seekToStartPosition(startPositionSeconds)
                    setOnCompletionListener { onCompleted() }
                    start()
                }
            }.getOrElse { throwable ->
                pendingPlayer?.release()
                throw throwable
            }

            player = newPlayer
            newPlayer.duration
        }.onFailure {
            release()
        }

    override fun currentPositionSeconds(): Int = player?.currentPosition?.toSeconds() ?: 0

    override fun release() {
        player?.runCatching { stop() }
        player?.release()
        player = null
    }

    private fun MediaPlayer.seekToStartPosition(startPositionSeconds: Int) {
        if (startPositionSeconds > 0) {
            seekTo(startPositionSeconds * MILLIS_PER_SECOND)
        }
    }

    private companion object {
        const val MILLIS_PER_SECOND = 1_000
    }
}

internal fun Int.toSeconds(): Int = this / 1_000
