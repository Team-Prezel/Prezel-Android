package com.team.prezel.core.designsystem.component.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewColumn
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun PrezelPlayerResourceTrack(
    durationMillis: Long,
    currentMillis: Long,
    markers: ImmutableList<PrezelPlayerResourceMarkerItem>,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    type: PrezelPlayerResourceTrackType = PrezelPlayerResourceTrackType.SPEECH,
    idle: Boolean = false,
    showHandle: Boolean = false,
    onDragStarted: () -> Unit = {},
    onDragStopped: () -> Unit = {},
) {
    val displayedDurationMillis = durationMillis.coerceAtLeast(0L)
    val displayedCurrentMillis = if (idle) 0L else currentMillis.coercePlayerMillis(displayedDurationMillis)
    val displayedProgress = if (idle) 0f else displayedCurrentMillis.toPlayerProgress(displayedDurationMillis)
    val timelineContentDescription = stringResource(type.contentDescriptionResId)

    Column(
        modifier = modifier.height(40.dp),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
    ) {
        PrezelPlayerTimeline(
            progress = displayedProgress,
            durationMillis = displayedDurationMillis,
            markers = markers,
            type = type,
            contentDescription = timelineContentDescription,
            showHandle = showHandle,
            onSeek = onSeek,
            onDragStarted = onDragStarted,
            onDragStopped = onDragStopped,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )

        PlayerTrackTimeLabels(
            currentMillis = displayedCurrentMillis,
            durationMillis = displayedDurationMillis,
        )
    }
}

@Composable
private fun PlayerTrackTimeLabels(
    currentMillis: Long,
    durationMillis: Long,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = currentMillis.formatPlayerTime(),
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

private fun Long.toPlayerProgress(durationMillis: Long): Float =
    if (durationMillis <= 0L) {
        0f
    } else {
        (toFloat() / durationMillis.toFloat()).coerceIn(0f, 1f)
    }

private fun Long.coercePlayerMillis(durationMillis: Long): Long = coerceIn(0L, durationMillis.coerceAtLeast(0L))

private fun Long.formatPlayerTime(): String {
    val totalSeconds = coerceAtLeast(0L) / 1_000L
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

private val PrezelPlayerResourceTrackType.contentDescriptionResId: Int
    get() = when (this) {
        PrezelPlayerResourceTrackType.SPEECH -> R.string.core_designsystem_player_speech_track_desc
        PrezelPlayerResourceTrackType.SCRIPT_MATCH -> R.string.core_designsystem_player_script_match_track_desc
    }

@BasicPreview
@Composable
private fun PrezelPlayerResourceTrackPreview() {
    PrezelTheme {
        PreviewSection(title = "Player Resource Track") {
            PreviewColumn {
                PlayerResourceTrackPreviewItem(name = "type=speech / idle=off / showHandle=off") {
                    PrezelPlayerResourceTrack(
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
    PrezelPlayerResourceMarkerItem(
        timeSeconds = 151,
        trackType = PrezelPlayerResourceTrackType.SPEECH,
        markerType = PrezelPlayerResourceMarkerType.WARNING,
    ),
    PrezelPlayerResourceMarkerItem(
        timeSeconds = 317,
        trackType = PrezelPlayerResourceTrackType.SPEECH,
        markerType = PrezelPlayerResourceMarkerType.GOOD,
    ),
    PrezelPlayerResourceMarkerItem(
        timeSeconds = 443,
        trackType = PrezelPlayerResourceTrackType.SPEECH,
        markerType = PrezelPlayerResourceMarkerType.WARNING,
    ),
)

private val previewScriptMatchMarkers = persistentListOf(
    PrezelPlayerResourceMarkerItem(
        timeSeconds = 317,
        trackType = PrezelPlayerResourceTrackType.SCRIPT_MATCH,
        markerType = PrezelPlayerResourceMarkerType.GOOD,
    ),
    PrezelPlayerResourceMarkerItem(
        timeSeconds = 443,
        trackType = PrezelPlayerResourceTrackType.SCRIPT_MATCH,
        markerType = PrezelPlayerResourceMarkerType.NEUTRAL,
    ),
)

private val previewOverlappingMarkers = persistentListOf(
    PrezelPlayerResourceMarkerItem(
        timeSeconds = 317,
        trackType = PrezelPlayerResourceTrackType.SPEECH,
        markerType = PrezelPlayerResourceMarkerType.GOOD,
    ),
    PrezelPlayerResourceMarkerItem(
        timeSeconds = 438,
        trackType = PrezelPlayerResourceTrackType.SPEECH,
        markerType = PrezelPlayerResourceMarkerType.GOOD,
    ),
    PrezelPlayerResourceMarkerItem(
        timeSeconds = 151,
        trackType = PrezelPlayerResourceTrackType.SPEECH,
        markerType = PrezelPlayerResourceMarkerType.WARNING,
    ),
    PrezelPlayerResourceMarkerItem(
        timeSeconds = 443,
        trackType = PrezelPlayerResourceTrackType.SPEECH,
        markerType = PrezelPlayerResourceMarkerType.WARNING,
    ),
)
