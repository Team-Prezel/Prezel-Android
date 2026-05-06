package com.team.prezel.core.designsystem.component.player

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.team.prezel.core.designsystem.preview.PreviewColumn
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.roundToInt

@Immutable
enum class PrezelPlayerResourceTrackType {
    SPEECH,
    SCRIPT_MATCH,
}

@Immutable
enum class PrezelSpeechMarkerType {
    GOOD,
    WARNING,
}

@Immutable
enum class PrezelScriptMatchMarkerType {
    GOOD,
    NEUTRAL,
}

@Immutable
class PrezelPlayerResourceMarkerItem private constructor(
    @param:FloatRange(from = 0.0, to = 1.0)
    val position: Float,
    val type: PrezelPlayerResourceMarkerType,
    internal val trackType: PrezelPlayerResourceTrackType,
) {
    companion object {
        fun speech(
            @FloatRange(from = 0.0, to = 1.0) position: Float,
            type: PrezelSpeechMarkerType,
        ): PrezelPlayerResourceMarkerItem =
            PrezelPlayerResourceMarkerItem(
                position = position,
                type = type.markerType,
                trackType = PrezelPlayerResourceTrackType.SPEECH,
            )

        fun scriptMatch(
            @FloatRange(from = 0.0, to = 1.0) position: Float,
            type: PrezelScriptMatchMarkerType,
        ): PrezelPlayerResourceMarkerItem =
            PrezelPlayerResourceMarkerItem(
                position = position,
                type = type.markerType,
                trackType = PrezelPlayerResourceTrackType.SCRIPT_MATCH,
            )
    }
}

@Composable
fun PrezelPlayerResourceTrack(
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    durationMillis: Long,
    currentMillis: Long,
    markers: ImmutableList<PrezelPlayerResourceMarkerItem>,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    type: PrezelPlayerResourceTrackType = PrezelPlayerResourceTrackType.SPEECH,
    idle: Boolean = false,
    showHandle: Boolean = false,
) {
    val coercedProgress = progress.coerceIn(0f, 1f)
    val displayedProgress = if (idle) 0f else coercedProgress
    val displayedCurrentMillis = if (idle) 0L else currentMillis

    Column(
        modifier = modifier.height(PlayerTrackHeight),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
    ) {
        PrezelPlayerTimeline(
            progress = displayedProgress,
            markers = markers,
            type = type,
            showHandle = showHandle,
            onSeek = onSeek,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = displayedCurrentMillis.formatPlayerTime(),
                style = PrezelTheme.typography.caption2Medium,
                color = PrezelTheme.colors.textRegular,
            )
            Text(
                text = durationMillis.formatPlayerTime(),
                style = PrezelTheme.typography.caption2Medium,
                color = PrezelTheme.colors.textRegular,
            )
        }
    }
}

@Composable
private fun PrezelPlayerTimeline(
    progress: Float,
    markers: ImmutableList<PrezelPlayerResourceMarkerItem>,
    type: PrezelPlayerResourceTrackType,
    showHandle: Boolean,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    var widthPx by remember { mutableIntStateOf(0) }

    fun seekTo(offsetX: Float) {
        if (widthPx > 0) onSeek((offsetX / widthPx).coerceIn(0f, 1f))
    }

    BoxWithConstraints(
        modifier = modifier.playerTimelineModifier(
            progress = progress,
            type = type,
            onWidthChanged = { widthPx = it },
            onSeekTo = ::seekTo,
        ),
        contentAlignment = Alignment.CenterStart,
    ) {
        PlayerTimelineBar(
            progress = progress,
            playedBarVisible = !showHandle,
        )
        PlayerTimelineMarkers(markers = markers, type = type)

        if (showHandle) PlayerTimelineHandle(progress = progress, zIndex = markers.size + 1f)
    }
}

private fun Modifier.playerTimelineModifier(
    progress: Float,
    type: PrezelPlayerResourceTrackType,
    onWidthChanged: (Int) -> Unit,
    onSeekTo: (Float) -> Unit,
): Modifier =
    fillMaxWidth()
        .height(PlayerTimelineHeight)
        .onSizeChanged { onWidthChanged(it.width) }
        .pointerInput(onSeekTo) {
            detectTapGestures { offset -> onSeekTo(offset.x) }
        }.pointerInput(onSeekTo) {
            detectHorizontalDragGestures { change, _ -> onSeekTo(change.position.x) }
        }.semantics {
            progressBarRangeInfo = ProgressBarRangeInfo(current = progress, range = 0f..1f)
            contentDescription = type.contentDescription
        }

@Composable
private fun PlayerTimelineBar(
    progress: Float,
    playedBarVisible: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(PlayerBarHeight)
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
            .height(PlayerBarHeight)
            .clip(PrezelTheme.shapes.V1000)
            .background(PrezelTheme.colors.bgLarge),
    )
}

@Composable
private fun BoxWithConstraintsScope.PlayerTimelineMarkers(
    markers: ImmutableList<PrezelPlayerResourceMarkerItem>,
    type: PrezelPlayerResourceTrackType,
) {
    var visibleMarkerIndex = 0
    markers.forEach { marker ->
        if (marker.trackType == type) {
            PrezelPlayerResourceMarker(
                type = marker.type,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset {
                        val markerX = ((maxWidth.toPx() - PlayerMarkerSize.toPx()) * marker.position.coerceIn(0f, 1f)).roundToInt()
                        IntOffset(x = markerX, y = 0)
                    }.zIndex(visibleMarkerIndex.toFloat()),
            )
            visibleMarkerIndex += 1
        }
    }
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
                val handleX = ((maxWidth.toPx() - PlayerHandleSize.toPx()) * progress.coerceIn(0f, 1f)).roundToInt()
                IntOffset(x = handleX, y = 0)
            }.size(PlayerHandleSize)
            .clip(PrezelTheme.shapes.V1000)
            .background(PrezelTheme.colors.iconLarge)
            .zIndex(zIndex),
    )
}

private val PrezelPlayerResourceTrackType.contentDescription: String
    get() = when (this) {
        PrezelPlayerResourceTrackType.SPEECH -> "Speech track"
        PrezelPlayerResourceTrackType.SCRIPT_MATCH -> "Script match track"
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

private fun Long.formatPlayerTime(): String {
    val totalSeconds = coerceAtLeast(0L) / 1_000L
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

private val PlayerTrackHeight = 40.dp
private val PlayerTimelineHeight = 16.dp
private val PlayerBarHeight = 8.dp
private val PlayerHandleSize = 16.dp

@Preview(showBackground = true)
@Composable
private fun PrezelPlayerResourceTrackPreview() {
    PrezelTheme {
        PreviewSection(title = "Player Resource Track") {
            PreviewColumn {
                PlayerResourceTrackPreviewItem(name = "type=speech / idle=off / showHandle=off") {
                    PrezelPlayerResourceTrack(
                        progress = 0.671f,
                        durationMillis = 690_000,
                        currentMillis = 443_000,
                        markers = previewSpeechMarkers,
                        onSeek = {},
                        modifier = Modifier.width(320.dp),
                        type = PrezelPlayerResourceTrackType.SPEECH,
                        idle = false,
                        showHandle = false,
                    )
                }
                PlayerResourceTrackPreviewItem(name = "type=scriptMatch / idle=off / showHandle=off") {
                    PrezelPlayerResourceTrack(
                        progress = 0.671f,
                        durationMillis = 690_000,
                        currentMillis = 443_000,
                        markers = previewScriptMatchMarkers,
                        onSeek = {},
                        modifier = Modifier.width(320.dp),
                        type = PrezelPlayerResourceTrackType.SCRIPT_MATCH,
                        idle = false,
                        showHandle = false,
                    )
                }
                PlayerResourceTrackPreviewItem(name = "idle=on") {
                    PrezelPlayerResourceTrack(
                        progress = 0.671f,
                        durationMillis = 690_000,
                        currentMillis = 443_000,
                        markers = previewSpeechMarkers,
                        onSeek = {},
                        modifier = Modifier.width(320.dp),
                        idle = true,
                    )
                }
                PlayerResourceTrackPreviewItem(name = "showHandle=on") {
                    PrezelPlayerResourceTrack(
                        progress = 0.671f,
                        durationMillis = 690_000,
                        currentMillis = 443_000,
                        markers = previewSpeechMarkers,
                        onSeek = {},
                        modifier = Modifier.width(320.dp),
                        showHandle = true,
                    )
                }
                PlayerResourceTrackPreviewItem(name = "overlapping markers") {
                    PrezelPlayerResourceTrack(
                        progress = 0.671f,
                        durationMillis = 690_000,
                        currentMillis = 443_000,
                        markers = previewOverlappingMarkers,
                        onSeek = {},
                        modifier = Modifier.width(320.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerResourceTrackPreviewItem(
    name: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
    ) {
        Text(
            text = name,
            style = PrezelTheme.typography.body3Medium,
            color = PrezelTheme.colors.textMedium,
        )
        content()
    }
}

private val previewSpeechMarkers = persistentListOf(
    PrezelPlayerResourceMarkerItem.speech(position = 0.2188f, type = PrezelSpeechMarkerType.WARNING),
    PrezelPlayerResourceMarkerItem.speech(position = 0.4594f, type = PrezelSpeechMarkerType.GOOD),
    PrezelPlayerResourceMarkerItem.speech(position = 0.6469f, type = PrezelSpeechMarkerType.WARNING),
)

private val previewScriptMatchMarkers = persistentListOf(
    PrezelPlayerResourceMarkerItem.scriptMatch(position = 0.4594f, type = PrezelScriptMatchMarkerType.GOOD),
    PrezelPlayerResourceMarkerItem.scriptMatch(position = 0.6469f, type = PrezelScriptMatchMarkerType.NEUTRAL),
)

private val previewOverlappingMarkers = persistentListOf(
    PrezelPlayerResourceMarkerItem.speech(position = 0.4594f, type = PrezelSpeechMarkerType.GOOD),
    PrezelPlayerResourceMarkerItem.speech(position = 0.6344f, type = PrezelSpeechMarkerType.GOOD),
    PrezelPlayerResourceMarkerItem.speech(position = 0.2188f, type = PrezelSpeechMarkerType.WARNING),
    PrezelPlayerResourceMarkerItem.speech(position = 0.6469f, type = PrezelSpeechMarkerType.WARNING),
)
