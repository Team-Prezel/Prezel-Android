package com.team.prezel.core.designsystem.component.player

import androidx.annotation.FloatRange
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
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
            progress = progress,
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
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    durationMillis: Long,
    currentMillis: Long,
    markers: ImmutableList<PrezelPlayerResourceMarkerItem>,
    trackType: PrezelPlayerResourceTrackType,
    idle: Boolean,
    showHandle: Boolean,
    onSeek: (Float) -> Unit,
) {
    PrezelPlayerResourceTrack(
        progress = progress,
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
        PrezelPlayerSeekButton(iconResId = PrezelIcons.SkipBackward, onClick = onBackwardClick)
        PrezelPlayerPlayPauseButton(
            playing = playing,
            onClick = onPlayPauseClick,
            modifier = Modifier.weight(1f),
        )
        PrezelPlayerSeekButton(iconResId = PrezelIcons.SkipForward, onClick = onForwardClick)
    }
}

@Composable
private fun PrezelPlayerSeekButton(
    iconResId: Int,
    onClick: () -> Unit,
) {
    PrezelIconButton(
        iconResId = iconResId,
        type = ButtonType.GHOST,
        hierarchy = ButtonHierarchy.SECONDARY,
        onClick = onClick,
        modifier = Modifier.widthIn(min = 80.dp),
    )
}

@Composable
private fun PrezelPlayerPlayPauseButton(
    playing: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrezelIconButton(
        iconResId = if (playing) PrezelIcons.Pause else PrezelIcons.Play,
        onClick = onClick,
        modifier = modifier,
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
                    progress = 0.671f,
                    durationMillis = 690_000,
                    currentMillis = 443_000,
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
                    progress = 0.671f,
                    durationMillis = 690_000,
                    currentMillis = 443_000,
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
        var currentMillis by remember { mutableLongStateOf(443_000L) }
        val durationMillis = 690_000L
        val progress = (currentMillis.toFloat() / durationMillis).coerceIn(0f, 1f)

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
                    progress = progress,
                    durationMillis = durationMillis,
                    currentMillis = currentMillis,
                    markers = previewMarkers,
                    onPlayPauseClick = { playing = !playing },
                    onBackwardClick = { currentMillis = (currentMillis - 5_000L).coerceAtLeast(0L) },
                    onForwardClick = { currentMillis = (currentMillis + 5_000L).coerceAtMost(durationMillis) },
                    onSeek = { seekProgress -> currentMillis = (durationMillis * seekProgress).toLong() },
                    modifier = Modifier.width(360.dp),
                    showHandle = true,
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
    PrezelPlayerResourceMarkerItem.speech(position = 0.2188f, type = PrezelSpeechMarkerType.WARNING),
    PrezelPlayerResourceMarkerItem.speech(position = 0.4594f, type = PrezelSpeechMarkerType.GOOD),
    PrezelPlayerResourceMarkerItem.speech(position = 0.6469f, type = PrezelSpeechMarkerType.WARNING),
)
