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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.component.actions.button.PrezelIconButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay

@Composable
fun PrezelPlayer(
    playing: Boolean,
    durationMillis: Long,
    currentMillis: Long,
    markers: ImmutableList<PrezelPlayerResourceMarkerItem>,
    onPlayPauseClick: () -> Unit,
    onBackwardClick: () -> Unit,
    onForwardClick: () -> Unit,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    trackType: PrezelPlayerResourceTrackType = PrezelPlayerResourceTrackType.SPEECH,
    idle: Boolean = false,
    showHandle: Boolean = false,
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
        )

        PrezelPlayerControls(
            playing = playing,
            onPlayPauseClick = onPlayPauseClick,
            onBackwardClick = onBackwardClick,
            onForwardClick = onForwardClick,
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
) {
    PrezelPlayerResourceTrack(
        durationMillis = durationMillis,
        currentMillis = currentMillis,
        markers = markers,
        type = trackType,
        idle = idle,
        showHandle = showHandle,
        onSeek = onSeek,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PrezelTheme.spacing.V20),
    )
}

@Composable
private fun PrezelPlayerControls(
    playing: Boolean,
    onPlayPauseClick: () -> Unit,
    onBackwardClick: () -> Unit,
    onForwardClick: () -> Unit,
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
            contentDescription = stringResource(R.string.core_designsystem_player_backward_desc),
            onClick = onBackwardClick,
        )
        PrezelPlayerPlayPauseButton(
            playing = playing,
            onClick = onPlayPauseClick,
            modifier = Modifier.weight(1f),
        )
        PrezelPlayerSeekButton(
            iconResId = PrezelIcons.SkipForward,
            contentDescription = stringResource(R.string.core_designsystem_player_forward_desc),
            onClick = onForwardClick,
        )
    }
}

@Composable
private fun PrezelPlayerSeekButton(
    iconResId: Int,
    contentDescription: String,
    onClick: () -> Unit,
) {
    PrezelIconButton(
        iconResId = iconResId,
        type = ButtonType.GHOST,
        hierarchy = ButtonHierarchy.SECONDARY,
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

@Preview(showBackground = true)
@Composable
private fun PrezelPlayerPreview() {
    PrezelTheme {
        PreviewSection(title = "Player") {
            PlayerPreviewItem(name = "playing=off") {
                PrezelPlayer(
                    playing = false,
                    durationMillis = 690_000L,
                    currentMillis = 100_000L,
                    markers = previewMarkers,
                    onPlayPauseClick = {},
                    onBackwardClick = {},
                    onForwardClick = {},
                    onSeek = {},
                    modifier = Modifier.width(360.dp),
                )
            }
            PlayerPreviewItem(name = "playing=on") {
                PrezelPlayer(
                    playing = true,
                    durationMillis = 690_000L,
                    currentMillis = 100_000L,
                    markers = previewMarkers,
                    onPlayPauseClick = {},
                    onBackwardClick = {},
                    onForwardClick = {},
                    onSeek = {},
                    modifier = Modifier.width(360.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrezelPlayerPlaybackPreview() {
    PrezelTheme {
        var playing by remember { mutableStateOf(false) }
        var currentMillis by remember { mutableLongStateOf(100_000L) }
        val durationMillis = 690_000L

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
                    playing = playing,
                    durationMillis = durationMillis,
                    currentMillis = currentMillis,
                    markers = previewMarkers,
                    onPlayPauseClick = { playing = !playing },
                    onBackwardClick = { currentMillis = (currentMillis - 5_000L).coerceAtLeast(0L) },
                    onForwardClick = { currentMillis = (currentMillis + 5_000L).coerceAtMost(durationMillis) },
                    onSeek = { seekProgress -> currentMillis = (durationMillis * seekProgress).toLong() },
                    modifier = Modifier.width(360.dp),
                    showHandle = false,
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
    PrezelPlayerResourceMarkerItem.speech(timeSeconds = 151, type = PrezelSpeechMarkerType.WARNING),
    PrezelPlayerResourceMarkerItem.speech(timeSeconds = 317, type = PrezelSpeechMarkerType.GOOD),
    PrezelPlayerResourceMarkerItem.speech(timeSeconds = 443, type = PrezelSpeechMarkerType.WARNING),
)
