package com.team.prezel.core.designsystem.component.player

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.ImmutableList
import kotlin.math.roundToInt

@Composable
internal fun PrezelPlayerTimeline(
    progress: Float,
    durationMillis: Long,
    markers: ImmutableList<PrezelPlayerResourceMarkerItem>,
    type: PrezelPlayerResourceTrackType,
    contentDescription: String,
    showHandle: Boolean,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    var widthPx by remember { mutableIntStateOf(0) }
    var dragging by remember { mutableStateOf(false) }
    val handleVisible = showHandle || dragging

    fun seekTo(offsetX: Float) {
        if (widthPx > 0) onSeek((offsetX / widthPx).coerceIn(0f, 1f))
    }

    BoxWithConstraints(
        modifier = modifier.playerTimelineModifier(
            progress = progress,
            contentDescription = contentDescription,
            onWidthChanged = { widthPx = it },
            onSeekTo = ::seekTo,
            onSetProgress = onSeek,
            onDragStarted = { offsetX ->
                dragging = true
                seekTo(offsetX)
            },
            onDragStopped = { dragging = false },
        ),
        contentAlignment = Alignment.CenterStart,
    ) {
        PlayerTimelineBar(
            progress = progress,
            playedBarVisible = !handleVisible,
        )
        PlayerTimelineMarkers(
            markers = markers,
            type = type,
            durationMillis = durationMillis,
        )

        if (handleVisible) PlayerTimelineHandle(progress = progress, zIndex = markers.size + 1f)
    }
}

private fun Modifier.playerTimelineModifier(
    progress: Float,
    contentDescription: String,
    onWidthChanged: (Int) -> Unit,
    onSeekTo: (Float) -> Unit,
    onSetProgress: (Float) -> Unit,
    onDragStarted: (Float) -> Unit,
    onDragStopped: () -> Unit,
): Modifier =
    fillMaxWidth()
        .height(16.dp)
        .onSizeChanged { onWidthChanged(it.width) }
        .pointerInput(onSeekTo) {
            detectTapGestures { offset -> onSeekTo(offset.x) }
        }.pointerInput(onSeekTo, onDragStarted, onDragStopped) {
            detectHorizontalDragGestures(
                onDragStart = { offset -> onDragStarted(offset.x) },
                onDragEnd = onDragStopped,
                onDragCancel = onDragStopped,
                onHorizontalDrag = { change, _ ->
                    onSeekTo(change.position.x)
                    change.consume()
                },
            )
        }.semantics {
            progressBarRangeInfo = ProgressBarRangeInfo(current = progress, range = 0f..1f)
            this.contentDescription = contentDescription
            setProgress { targetProgress ->
                onSetProgress(targetProgress.coerceIn(0f, 1f))
                true
            }
        }

@Composable
private fun PlayerTimelineBar(
    progress: Float,
    playedBarVisible: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(PrezelTheme.shapes.V1000)
            .background(PrezelTheme.colors.bgMedium),
    )

    if (playedBarVisible) PlayerTimelinePlayedBar(progress = progress)
}

@Composable
private fun PlayerTimelinePlayedBar(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth(progress)
            .height(8.dp)
            .clip(PrezelTheme.shapes.V1000)
            .background(PrezelTheme.colors.bgLarge),
    )
}

@Composable
private fun BoxWithConstraintsScope.PlayerTimelineMarkers(
    markers: ImmutableList<PrezelPlayerResourceMarkerItem>,
    type: PrezelPlayerResourceTrackType,
    durationMillis: Long,
) {
    var visibleMarkerIndex = 0
    markers.forEach { marker ->
        if (marker.matchesTrackType(type)) {
            PrezelPlayerResourceMarker(
                type = marker.resourceMarkerType,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset {
                        val markerX = ((maxWidth.toPx() - 8.dp.toPx()) * marker.progressIn(durationMillis)).roundToInt()
                        IntOffset(x = markerX, y = 0)
                    }.zIndex(visibleMarkerIndex.toFloat()),
            )
            visibleMarkerIndex += 1
        }
    }
}

private fun PrezelPlayerResourceMarkerItem.matchesTrackType(type: PrezelPlayerResourceTrackType): Boolean =
    when (type) {
        PrezelPlayerResourceTrackType.SPEECH -> this is PrezelPlayerResourceMarkerItem.Speech
        PrezelPlayerResourceTrackType.SCRIPT_MATCH -> this is PrezelPlayerResourceMarkerItem.ScriptMatch
    }

private val PrezelPlayerResourceMarkerItem.resourceMarkerType: PrezelPlayerResourceMarkerType
    get() = when (this) {
        is PrezelPlayerResourceMarkerItem.Speech -> type.markerType
        is PrezelPlayerResourceMarkerItem.ScriptMatch -> type.markerType
    }

private fun PrezelPlayerResourceMarkerItem.progressIn(durationMillis: Long): Float =
    if (durationMillis <= 0L) {
        0f
    } else {
        (timeSeconds * 1_000f / durationMillis).coerceIn(0f, 1f)
    }

@Composable
private fun BoxWithConstraintsScope.PlayerTimelineHandle(
    progress: Float,
    zIndex: Float,
) {
    Box(
        modifier = Modifier
            .align(Alignment.CenterStart)
            .offset {
                val handleX = ((maxWidth.toPx() - 16.dp.toPx()) * progress.coerceIn(0f, 1f)).roundToInt()
                IntOffset(x = handleX, y = 0)
            }.size(16.dp)
            .clip(PrezelTheme.shapes.V1000)
            .background(PrezelTheme.colors.iconLarge)
            .zIndex(zIndex),
    )
}

private val PrezelSpeechMarkerType.markerType: PrezelPlayerResourceMarkerType
    get() = when (this) {
        PrezelSpeechMarkerType.GOOD -> PrezelPlayerResourceMarkerType.GOOD
        PrezelSpeechMarkerType.WARNING -> PrezelPlayerResourceMarkerType.WARNING
    }

private val PrezelScriptMatchMarkerType.markerType: PrezelPlayerResourceMarkerType
    get() = when (this) {
        PrezelScriptMatchMarkerType.GOOD -> PrezelPlayerResourceMarkerType.GOOD
        PrezelScriptMatchMarkerType.NEUTRAL -> PrezelPlayerResourceMarkerType.NEUTRAL
    }
