package com.team.prezel.core.designsystem.component.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun rememberPrezelPlayerState(
    durationMillis: Long,
    initialItems: ImmutableList<PrezelPlayerItem>,
    playing: Boolean = false,
    currentMillis: Long = 0L,
): PrezelPlayerState =
    rememberSaveable(
        saver = PrezelPlayerState.Saver,
    ) {
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

    private val playbackEnded: Boolean
        get() = currentMillis >= durationMillis

    private val currentItemIndex: Int
        get() = if (items.isEmpty()) {
            -1
        } else {
            items.indexOfLast { item -> currentMillis >= item.timeMillis }.coerceAtLeast(0)
        }

    val previousEnabled: Boolean
        get() {
            val currentItem = items.getOrNull(currentItemIndex) ?: return false
            return currentItemIndex > 0 || currentMillis > currentItem.timeMillis
        }

    val nextEnabled: Boolean
        get() = currentItemIndex >= 0 && currentItemIndex < items.lastIndex

    fun seekToProgress(progress: Float) {
        seekToMillis((durationMillis * progress.coerceIn(0f, 1f)).toLong())
    }

    fun moveToPreviousItem() {
        val currentItem = items.getOrNull(currentItemIndex) ?: return
        val targetIndex = if (currentMillis > currentItem.timeMillis) {
            currentItemIndex
        } else {
            currentItemIndex - 1
        }

        if (targetIndex in items.indices) seekToMillis(items[targetIndex].timeMillis)
    }

    fun moveToNextItem() {
        if (nextEnabled) seekToMillis(items[currentItemIndex + 1].timeMillis)
    }

    fun play() {
        if (playbackEnded) seekToMillis(0L)
        playing = true
    }

    fun pause() {
        playing = false
    }

    fun togglePlaying() {
        if (playing) {
            pause()
        } else {
            play()
        }
    }

    fun updateCurrentMillis(currentMillis: Long) {
        this.currentMillis = currentMillis.coerceIn(0L, durationMillis)
    }

    fun startDrag() {
        dragging = true
    }

    fun stopDrag() {
        dragging = false
    }

    private fun seekToMillis(targetMillis: Long) {
        currentMillis = targetMillis.coerceIn(0L, durationMillis)
    }

    companion object {
        val Saver: Saver<PrezelPlayerState, Any> = Saver(
            save = { state ->
                listOf(
                    state.playing,
                    state.durationMillis,
                    state.currentMillis,
                    state.items.map { item -> item.toSaveable() },
                )
            },
            restore = { restored ->
                val values = restored as List<*>
                PrezelPlayerState(
                    playing = values[0] as Boolean,
                    durationMillis = values[1] as Long,
                    currentMillis = values[2] as Long,
                    items = (values[3] as List<*>)
                        .map { savedItem -> (savedItem as List<*>).toPrezelPlayerItem() }
                        .toImmutableList(),
                )
            },
        )

        private fun PrezelPlayerItem.toSaveable(): List<Any> =
            when (this) {
                is PrezelPlayerItem.Segment -> listOf(
                    PLAYER_ITEM_TYPE_SEGMENT,
                    timeMillis,
                )

                is PrezelPlayerItem.Marker -> listOf(
                    PLAYER_ITEM_TYPE_MARKER,
                    timeMillis,
                    markerType.name,
                )
            }

        private fun List<*>.toPrezelPlayerItem(): PrezelPlayerItem {
            val type = this[0] as String
            val timeMillis = this[1] as Long

            return when (type) {
                PLAYER_ITEM_TYPE_SEGMENT -> PrezelPlayerItem.Segment(
                    timeMillis = timeMillis,
                )

                PLAYER_ITEM_TYPE_MARKER -> PrezelPlayerItem.Marker(
                    timeMillis = timeMillis,
                    markerType = PrezelPlayerMarkerType.valueOf(this[2] as String),
                )

                else -> error("지원하지 않는 PrezelPlayerItem 타입입니다: $type")
            }
        }

        private const val PLAYER_ITEM_TYPE_SEGMENT = "segment"
        private const val PLAYER_ITEM_TYPE_MARKER = "marker"
    }
}
