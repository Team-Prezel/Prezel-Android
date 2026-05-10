package com.team.prezel.core.designsystem.component.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.collections.immutable.ImmutableList

@Composable
fun rememberPrezelPlayerState(
    durationMillis: Long,
    initialItems: ImmutableList<PrezelPlayerItem>,
    playing: Boolean = false,
    currentMillis: Long = 0L,
): PrezelPlayerState =
    remember {
        PrezelPlayerState(
            playing = playing,
            durationMillis = durationMillis,
            currentMillis = currentMillis,
            items = initialItems,
        )
    }

@Stable
class PrezelPlayerState internal constructor(
    playing: Boolean,
    durationMillis: Long,
    currentMillis: Long,
    items: ImmutableList<PrezelPlayerItem>,
) {
    var playing by mutableStateOf(playing)
        private set

    var durationMillis by mutableLongStateOf(durationMillis)
        private set

    var currentMillis by mutableLongStateOf(currentMillis)
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

    fun seekToProgress(progress: Float) {
        seekToMillis((durationMillis * progress.coerceIn(0f, 1f)).toLong())
    }

    fun moveToPreviousItem() {
        if (previousEnabled) seekToMillis(items[currentItemIndex - 1].timeMillis)
    }

    fun moveToNextItem() {
        if (nextEnabled) seekToMillis(items[currentItemIndex + 1].timeMillis)
    }

    fun updatePlaying(playing: Boolean) {
        this.playing = playing
    }

    fun togglePlaying() {
        playing = !playing
    }

    fun updateCurrentMillis(currentMillis: Long) {
        this.currentMillis = currentMillis
    }

    fun startDrag() {
        dragging = true
    }

    fun stopDrag() {
        dragging = false
    }

    private fun seekToMillis(targetMillis: Long) {
        currentMillis = targetMillis
    }
}
