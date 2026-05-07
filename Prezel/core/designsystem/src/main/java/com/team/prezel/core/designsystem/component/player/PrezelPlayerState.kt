package com.team.prezel.core.designsystem.component.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlinx.collections.immutable.ImmutableList

@Composable
fun rememberPrezelPlayerState(
    playing: Boolean,
    durationMillis: Long,
    currentMillis: Long,
    items: ImmutableList<PrezelPlayerItem>,
    onPlayPauseClick: () -> Unit,
    onSeekToMillis: (Long) -> Unit,
): PrezelPlayerState {
    val currentOnPlayPauseClick = rememberUpdatedState(onPlayPauseClick)
    val currentOnSeekToMillis = rememberUpdatedState(onSeekToMillis)

    val state = remember {
        PrezelPlayerState(
            playing = playing,
            durationMillis = durationMillis,
            currentMillis = currentMillis,
            items = items,
            onPlayPauseClick = { currentOnPlayPauseClick.value() },
            onSeekToMillis = { targetMillis -> currentOnSeekToMillis.value(targetMillis) },
        )
    }

    SideEffect {
        state.update(
            playing = playing,
            durationMillis = durationMillis,
            currentMillis = currentMillis,
            items = items,
        )
    }

    return state
}

@Stable
class PrezelPlayerState internal constructor(
    playing: Boolean,
    durationMillis: Long,
    currentMillis: Long,
    items: ImmutableList<PrezelPlayerItem>,
    private val onPlayPauseClick: () -> Unit,
    private val onSeekToMillis: (Long) -> Unit,
) {
    var playing by mutableStateOf(playing)
        private set

    var durationMillis by mutableLongStateOf(durationMillis.coerceAtLeast(0L))
        private set

    var currentMillis by mutableLongStateOf(currentMillis.coercePlayerMillis(this.durationMillis))
        private set

    var items by mutableStateOf(items)
        private set

    var dragging by mutableStateOf(false)
        private set

    val idle: Boolean
        get() = !playing && currentMillis == 0L

    val showHandle: Boolean
        get() = dragging

    private val currentItemIndex: Int
        get() = if (items.isEmpty()) {
            -1
        } else {
            items.indexOfLast { item -> currentMillis >= item.timeMillis }.coerceAtLeast(0)
        }

    val previousEnabled: Boolean
        get() = currentItemIndex > 0

    val nextEnabled: Boolean
        get() = currentItemIndex >= 0 && currentItemIndex < items.lastIndex

    fun playPause() {
        onPlayPauseClick()
    }

    fun seekToProgress(progress: Float) {
        seekToMillis((durationMillis * progress.coerceIn(0f, 1f)).toLong())
    }

    fun moveToPreviousItem() {
        if (previousEnabled) seekToMillis(items[currentItemIndex - 1].timeMillis)
    }

    fun moveToNextItem() {
        if (nextEnabled) seekToMillis(items[currentItemIndex + 1].timeMillis)
    }

    fun startDrag() {
        dragging = true
    }

    fun stopDrag() {
        dragging = false
    }

    private fun seekToMillis(targetMillis: Long) {
        val coercedMillis = targetMillis.coercePlayerMillis(durationMillis)
        if (dragging) currentMillis = coercedMillis
        onSeekToMillis(coercedMillis)
    }

    internal fun update(
        playing: Boolean,
        durationMillis: Long,
        currentMillis: Long,
        items: ImmutableList<PrezelPlayerItem>,
    ) {
        this.playing = playing
        this.durationMillis = durationMillis.coerceAtLeast(0L)
        if (!dragging) {
            this.currentMillis = currentMillis.coercePlayerMillis(this.durationMillis)
        } else {
            this.currentMillis = this.currentMillis.coercePlayerMillis(this.durationMillis)
        }
        this.items = items
    }
}

private fun Long.coercePlayerMillis(durationMillis: Long): Long = coerceIn(0L, durationMillis.coerceAtLeast(0L))
