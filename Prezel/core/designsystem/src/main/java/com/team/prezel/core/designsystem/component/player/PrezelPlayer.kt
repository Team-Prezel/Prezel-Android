package com.team.prezel.core.designsystem.component.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.component.actions.button.PrezelIconButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay

@Composable
fun PrezelPlayer(
    state: PrezelPlayerState,
    markers: ImmutableList<PrezelPlayerResourceMarkerItem>,
    modifier: Modifier = Modifier,
    trackType: PrezelPlayerResourceTrackType = PrezelPlayerResourceTrackType.SPEECH,
) {
    PrezelPlayerContent(
        playing = state.playing,
        durationMillis = state.durationMillis,
        currentMillis = state.currentMillis,
        markers = markers,
        onPlayPauseClick = state::playPause,
        onPreviousClick = state::moveToPreviousItem,
        onNextClick = state::moveToNextItem,
        onSeek = state::seekToProgress,
        modifier = modifier,
        trackType = trackType,
        idle = state.idle,
        showHandle = state.showHandle,
        onDragStarted = state::startDrag,
        onDragStopped = state::stopDrag,
        previousEnabled = state.previousEnabled,
        nextEnabled = state.nextEnabled,
    )
}

@Composable
private fun PrezelPlayerContent(
    playing: Boolean,
    durationMillis: Long,
    currentMillis: Long,
    markers: ImmutableList<PrezelPlayerResourceMarkerItem>,
    onPlayPauseClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    trackType: PrezelPlayerResourceTrackType = PrezelPlayerResourceTrackType.SPEECH,
    idle: Boolean = false,
    showHandle: Boolean = false,
    onDragStarted: () -> Unit = {},
    onDragStopped: () -> Unit = {},
    previousEnabled: Boolean = true,
    nextEnabled: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = PrezelTheme.spacing.V20),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
    ) {
        PrezelPlayerTrackSection(
            durationMillis = durationMillis,
            currentMillis = currentMillis,
            markers = markers,
            trackType = trackType,
            idle = idle,
            showHandle = showHandle,
            onSeek = onSeek,
            onDragStarted = onDragStarted,
            onDragStopped = onDragStopped,
        )

        PrezelPlayerControls(
            playing = playing,
            onPlayPauseClick = onPlayPauseClick,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            previousEnabled = previousEnabled,
            nextEnabled = nextEnabled,
        )
    }
}

@Composable
private fun PrezelPlayerTrackSection(
    durationMillis: Long,
    currentMillis: Long,
    markers: ImmutableList<PrezelPlayerResourceMarkerItem>,
    trackType: PrezelPlayerResourceTrackType,
    idle: Boolean,
    showHandle: Boolean,
    onSeek: (Float) -> Unit,
    onDragStarted: () -> Unit,
    onDragStopped: () -> Unit,
) {
    PrezelPlayerResourceTrack(
        durationMillis = durationMillis,
        currentMillis = currentMillis,
        markers = markers,
        type = trackType,
        idle = idle,
        showHandle = showHandle,
        onSeek = onSeek,
        onDragStarted = onDragStarted,
        onDragStopped = onDragStopped,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PrezelTheme.spacing.V20),
    )
}

@Composable
private fun PrezelPlayerControls(
    playing: Boolean,
    onPlayPauseClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    previousEnabled: Boolean,
    nextEnabled: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = PrezelTheme.spacing.V24,
            alignment = Alignment.CenterHorizontally,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PrezelPlayerSeekButton(
            iconResId = PrezelIcons.SkipBackward,
            contentDescription = stringResource(R.string.core_designsystem_player_previous_desc),
            enabled = previousEnabled,
            onClick = onPreviousClick,
        )
        PrezelPlayerPlayPauseButton(
            playing = playing,
            onClick = onPlayPauseClick,
            modifier = Modifier.weight(1f),
        )
        PrezelPlayerSeekButton(
            iconResId = PrezelIcons.SkipForward,
            contentDescription = stringResource(R.string.core_designsystem_player_next_desc),
            enabled = nextEnabled,
            onClick = onNextClick,
        )
    }
}

@Composable
private fun PrezelPlayerSeekButton(
    iconResId: Int,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    PrezelIconButton(
        iconResId = iconResId,
        type = ButtonType.GHOST,
        hierarchy = ButtonHierarchy.SECONDARY,
        enabled = enabled,
        onClick = onClick,
        modifier = Modifier
            .widthIn(min = 80.dp)
            .semantics { this.contentDescription = contentDescription },
    )
}

@Composable
private fun PrezelPlayerPlayPauseButton(
    playing: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val playContentDescription = stringResource(R.string.core_designsystem_player_play_desc)
    val pauseContentDescription = stringResource(R.string.core_designsystem_player_pause_desc)

    PrezelIconButton(
        iconResId = if (playing) PrezelIcons.Pause else PrezelIcons.Play,
        onClick = onClick,
        modifier = modifier.semantics {
            contentDescription = if (playing) {
                pauseContentDescription
            } else {
                playContentDescription
            }
        },
        buttonDefault = PrezelButtonDefaults.getDefault(
            isIconOnly = true,
            type = ButtonType.FILLED,
            size = ButtonSize.REGULAR,
            hierarchy = ButtonHierarchy.PRIMARY,
            isRounded = true,
            contentColor = if (playing) PrezelTheme.colors.iconRegular else PrezelTheme.colors.solidWhite,
            backgroundColor = if (playing) PrezelTheme.colors.bgLarge else PrezelTheme.colors.interactiveRegular,
        ),
    )
}

@BasicPreview
@Composable
private fun PrezelPlayerPreview() {
    PrezelTheme {
        PreviewSection(title = "Player") {
            PlayerPreviewItem(name = "playing=off") {
                PrezelPlayer(
                    state = rememberPrezelPlayerState(
                        playing = false,
                        durationMillis = 690_000L,
                        currentMillis = 0L,
                        items = previewPlayerItems,
                        onPlayPauseClick = {},
                        onSeekToMillis = {},
                    ),
                    markers = previewMarkers,
                    modifier = Modifier.width(360.dp),
                )
            }
            PlayerPreviewItem(name = "playing=on") {
                PrezelPlayer(
                    state = rememberPrezelPlayerState(
                        playing = true,
                        durationMillis = 690_000L,
                        currentMillis = 234_000L,
                        items = previewPlayerItems,
                        onPlayPauseClick = {},
                        onSeekToMillis = {},
                    ),
                    markers = previewMarkers,
                    modifier = Modifier.width(360.dp),
                )
            }
        }
    }
}

@BasicPreview
@Composable
private fun PrezelPlayerPlaybackPreview() {
    PrezelTheme {
        var playing by remember { mutableStateOf(false) }
        var currentMillis by remember { mutableLongStateOf(0L) }
        val durationMillis = 690_000L
        val playerState = rememberPrezelPlayerState(
            playing = playing,
            durationMillis = durationMillis,
            currentMillis = currentMillis,
            items = previewPlayerItems,
            onPlayPauseClick = { playing = !playing },
            onSeekToMillis = { targetMillis -> currentMillis = targetMillis },
        )

        LaunchedEffect(playing) {
            while (playing) {
                delay(1_000L)
                currentMillis = (currentMillis + 1_000L).coerceAtMost(durationMillis)
                if (currentMillis == durationMillis) playing = false
            }
        }

        PreviewSection(title = "Player Playback") {
            PlayerPreviewItem(name = if (playing) "playing" else "paused") {
                PrezelPlayer(
                    state = playerState,
                    markers = previewMarkers,
                    modifier = Modifier.width(360.dp),
                )
            }
        }
    }
}

@Composable
private fun PlayerPreviewItem(
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

private val previewMarkers = persistentListOf(
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

private val previewPlayerItems = persistentListOf(
    PrezelPlayerItem(id = "preview-script-item-0", startMillis = 0L),
    PrezelPlayerItem(id = "preview-script-item-1", startMillis = 151_000L),
    PrezelPlayerItem(id = "preview-script-item-2", startMillis = 317_000L),
    PrezelPlayerItem(id = "preview-script-item-3", startMillis = 443_000L),
)
