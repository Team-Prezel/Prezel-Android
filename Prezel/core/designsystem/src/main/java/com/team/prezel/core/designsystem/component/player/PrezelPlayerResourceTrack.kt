package com.team.prezel.core.designsystem.component.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
    items: ImmutableList<PrezelPlayerItem>,
    contentDescription: String,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    idle: Boolean = false,
    showHandle: Boolean = false,
    onDragStarted: () -> Unit = {},
    onDragStopped: () -> Unit = {},
) {
    val displayedCurrentMillis = if (idle) 0L else currentMillis
    val displayedProgress = if (idle) 0f else displayedCurrentMillis.toPlayerProgress(durationMillis)

    Column(
        modifier = modifier.heightIn(min = 40.dp),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
    ) {
        PrezelPlayerTimeline(
            progress = displayedProgress,
            durationMillis = durationMillis,
            items = items,
            contentDescription = contentDescription,
            showHandle = showHandle,
            onSeek = onSeek,
            onDragStarted = onDragStarted,
            onDragStopped = onDragStopped,
            modifier = Modifier.fillMaxWidth(),
        )

        PlayerTrackTimeLabels(
            currentMillis = displayedCurrentMillis,
            durationMillis = durationMillis,
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

private fun Long.formatPlayerTime(): String {
    val totalSeconds = this / 1_000L
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

@BasicPreview
@Composable
private fun PrezelPlayerResourceTrackPreview() {
    PrezelTheme {
        PreviewSection(title = "Player Resource Track") {
            PreviewColumn {
                PlayerResourceTrackPreviewItem(name = "speech / idle=off / showHandle=off") {
                    PrezelPlayerResourceTrack(
                        durationMillis = 690_000,
                        currentMillis = 443_000,
                        items = previewSpeechItems,
                        contentDescription = stringResource(R.string.core_designsystem_player_speech_track_desc),
                        onSeek = {},
                        modifier = Modifier.width(320.dp),
                        idle = false,
                        showHandle = false,
                    )
                }
                PlayerResourceTrackPreviewItem(name = "script match / idle=off / showHandle=off") {
                    PrezelPlayerResourceTrack(
                        durationMillis = 690_000,
                        currentMillis = 443_000,
                        items = previewScriptMatchItems,
                        contentDescription = stringResource(R.string.core_designsystem_player_script_match_track_desc),
                        onSeek = {},
                        modifier = Modifier.width(320.dp),
                        idle = false,
                        showHandle = false,
                    )
                }
                PlayerResourceTrackPreviewItem(name = "idle=on") {
                    PrezelPlayerResourceTrack(
                        durationMillis = 690_000,
                        currentMillis = 443_000,
                        items = previewSpeechItems,
                        contentDescription = stringResource(R.string.core_designsystem_player_speech_track_desc),
                        onSeek = {},
                        modifier = Modifier.width(320.dp),
                        idle = true,
                    )
                }
                PlayerResourceTrackPreviewItem(name = "showHandle=on") {
                    PrezelPlayerResourceTrack(
                        durationMillis = 690_000,
                        currentMillis = 443_000,
                        items = previewSpeechItems,
                        contentDescription = stringResource(R.string.core_designsystem_player_speech_track_desc),
                        onSeek = {},
                        modifier = Modifier.width(320.dp),
                        showHandle = true,
                    )
                }
                PlayerResourceTrackPreviewItem(name = "overlapping markers") {
                    PrezelPlayerResourceTrack(
                        durationMillis = 690_000,
                        currentMillis = 443_000,
                        items = previewOverlappingItems,
                        contentDescription = stringResource(R.string.core_designsystem_player_speech_track_desc),
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

private val previewSpeechItems = persistentListOf(
    PrezelPlayerItem.Segment(timeMillis = 0L),
    PrezelPlayerItem.Segment(timeMillis = 90_000L),
    PrezelPlayerItem.Marker(
        timeMillis = 151_000L,
        markerType = PrezelPlayerMarkerType.WARNING,
    ),
    PrezelPlayerItem.Segment(timeMillis = 234_000L),
    PrezelPlayerItem.Marker(
        timeMillis = 317_000L,
        markerType = PrezelPlayerMarkerType.GOOD,
    ),
    PrezelPlayerItem.Marker(
        timeMillis = 443_000L,
        markerType = PrezelPlayerMarkerType.WARNING,
    ),
)

private val previewScriptMatchItems = persistentListOf(
    PrezelPlayerItem.Segment(timeMillis = 0L),
    PrezelPlayerItem.Segment(timeMillis = 151_000L),
    PrezelPlayerItem.Segment(timeMillis = 234_000L),
    PrezelPlayerItem.Marker(
        timeMillis = 317_000L,
        markerType = PrezelPlayerMarkerType.GOOD,
    ),
    PrezelPlayerItem.Marker(
        timeMillis = 443_000L,
        markerType = PrezelPlayerMarkerType.NEUTRAL,
    ),
)

private val previewOverlappingItems = persistentListOf(
    PrezelPlayerItem.Segment(timeMillis = 0L),
    PrezelPlayerItem.Segment(timeMillis = 151_000L),
    PrezelPlayerItem.Marker(
        timeMillis = 317_000L,
        markerType = PrezelPlayerMarkerType.GOOD,
    ),
    PrezelPlayerItem.Segment(timeMillis = 360_000L),
    PrezelPlayerItem.Marker(
        timeMillis = 438_000L,
        markerType = PrezelPlayerMarkerType.GOOD,
    ),
    PrezelPlayerItem.Marker(
        timeMillis = 151_000L,
        markerType = PrezelPlayerMarkerType.WARNING,
    ),
    PrezelPlayerItem.Marker(
        timeMillis = 443_000L,
        markerType = PrezelPlayerMarkerType.WARNING,
    ),
)
