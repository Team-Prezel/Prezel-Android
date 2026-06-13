package com.team.prezel.feature.report.impl.accuracydetail

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
internal fun rememberRemoteAudioPlaybackState(audioUrl: String): RemoteAudioPlaybackState {
    val state = remember(audioUrl) { RemoteAudioPlaybackState(audioUrl = audioUrl) }

    DisposableEffect(state) {
        onDispose { state.release() }
    }

    return state
}

@Stable
internal class RemoteAudioPlaybackState(
    private val audioUrl: String,
) {
    private var mediaPlayer: MediaPlayer? = null
    private var lastKnownPositionMillis by mutableIntStateOf(0)
    private var pendingPositionMillis: Int? = null
    private var prepared by mutableStateOf(false)
    private var playWhenPrepared = false

    var playbackError by mutableStateOf(false)
        private set

    val currentPositionMillis: Int
        get() = if (prepared) {
            mediaPlayer
                ?.currentPosition
                ?.coerceAtLeast(0)
                ?: lastKnownPositionMillis
        } else {
            lastKnownPositionMillis
        }

    fun play(startPositionMillis: Int) {
        playbackError = false
        playWhenPrepared = true

        val player = mediaPlayer ?: preparePlayer() ?: return
        val positionMillis = startPositionMillis.coerceAtLeast(0)

        if (prepared) {
            startPreparedPlayer(player = player, positionMillis = positionMillis)
        } else {
            pendingPositionMillis = positionMillis
            lastKnownPositionMillis = positionMillis
        }
    }

    fun pause() {
        playWhenPrepared = false
        mediaPlayer?.runCatching {
            if (prepared && isPlaying) pause()
            lastKnownPositionMillis = currentPositionMillis
        }
    }

    fun seekTo(positionMillis: Int) {
        val targetPositionMillis = positionMillis.coerceAtLeast(0)
        pendingPositionMillis = targetPositionMillis
        lastKnownPositionMillis = targetPositionMillis

        val player = mediaPlayer ?: return
        if (prepared) seekPreparedPlayer(player = player, positionMillis = targetPositionMillis)
    }

    fun release() {
        playWhenPrepared = false
        prepared = false
        pendingPositionMillis = null
        mediaPlayer?.release()
        mediaPlayer = null
        lastKnownPositionMillis = 0
    }

    private fun preparePlayer(): MediaPlayer? =
        runCatching {
            MediaPlayer().apply {
                setOnPreparedListener { player ->
                    prepared = true
                    val positionMillis = pendingPositionMillis ?: lastKnownPositionMillis
                    pendingPositionMillis = null
                    if (playWhenPrepared) {
                        startPreparedPlayer(player = player, positionMillis = positionMillis)
                    } else {
                        seekPreparedPlayer(player = player, positionMillis = positionMillis)
                    }
                }
                setOnErrorListener { _, _, _ ->
                    handlePlaybackFailure()
                    true
                }
                setDataSource(audioUrl)
                prepareAsync()
                setOnCompletionListener {
                    lastKnownPositionMillis = duration.coerceAtLeast(0)
                    playWhenPrepared = false
                }
            }
        }.onFailure {
            handlePlaybackFailure()
        }.getOrNull()
            ?.also { mediaPlayer = it }

    private fun startPreparedPlayer(
        player: MediaPlayer,
        positionMillis: Int,
    ) {
        runCatching {
            player.seekTo(positionMillis)
            player.start()
            lastKnownPositionMillis = player.currentPosition.coerceAtLeast(0)
        }.onFailure {
            handlePlaybackFailure()
        }
    }

    private fun seekPreparedPlayer(
        player: MediaPlayer,
        positionMillis: Int,
    ) {
        runCatching {
            player.seekTo(positionMillis)
            lastKnownPositionMillis = positionMillis
        }.onFailure {
            handlePlaybackFailure()
        }
    }

    private fun handlePlaybackFailure() {
        release()
        playbackError = true
    }
}
