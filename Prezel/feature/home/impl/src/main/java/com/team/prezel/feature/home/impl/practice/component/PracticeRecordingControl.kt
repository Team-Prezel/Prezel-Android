package com.team.prezel.feature.home.impl.practice.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelIconButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingState

internal enum class PracticeRecordingControlState {
    READY_TO_RECORD,
    RECORDING,
    RECORDING_PAUSED,
    READY_TO_PLAY,
    PLAYING,
    PLAYBACK_PAUSED,
}

internal fun PracticeRecordingState.toControlState(): PracticeRecordingControlState =
    when (this) {
        PracticeRecordingState.Idle -> PracticeRecordingControlState.READY_TO_RECORD
        is PracticeRecordingState.Recording -> PracticeRecordingControlState.RECORDING
        is PracticeRecordingState.RecordingPaused -> PracticeRecordingControlState.RECORDING_PAUSED
        is PracticeRecordingState.ReadyToPlay -> PracticeRecordingControlState.READY_TO_PLAY
        is PracticeRecordingState.Playing -> PracticeRecordingControlState.PLAYING
        is PracticeRecordingState.PlaybackPaused -> PracticeRecordingControlState.PLAYBACK_PAUSED
    }

@Composable
internal fun PracticeRecordingControl(
    currentSeconds: Int,
    totalSeconds: Int,
    state: PracticeRecordingControlState,
    onStartRecording: () -> Unit,
    onPauseRecording: () -> Unit,
    onResumeRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onResetRecording: () -> Unit,
    onSelectAudioFile: () -> Unit,
    onStartPlayback: () -> Unit,
    onPausePlayback: () -> Unit,
    onResumePlayback: () -> Unit,
    onStopPlayback: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val actions = state.actions(
        onStartRecording = onStartRecording,
        onPauseRecording = onPauseRecording,
        onResumeRecording = onResumeRecording,
        onStopRecording = onStopRecording,
        onResetRecording = onResetRecording,
        onSelectAudioFile = onSelectAudioFile,
        onStartPlayback = onStartPlayback,
        onPausePlayback = onPausePlayback,
        onResumePlayback = onResumePlayback,
        onStopPlayback = onStopPlayback,
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PracticeRecordingTimeText(
            currentSeconds = currentSeconds,
            totalSeconds = totalSeconds,
            state = state,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            actions.forEach { action ->
                PrezelIconButton(
                    iconResId = action.iconResId,
                    modifier = Modifier.size(48.dp),
                    isRounded = true,
                    buttonDefault = PrezelButtonDefaults.getDefault(
                        isIconOnly = true,
                        isRounded = true,
                        type = ButtonType.FILLED,
                        size = ButtonSize.REGULAR,
                        hierarchy = ButtonHierarchy.SECONDARY,
                        contentColor = action.iconColor(),
                        backgroundColor = PrezelTheme.colors.bgLarge,
                        iconSize = 20.dp,
                    ),
                    onClick = action.onClick,
                )
            }
        }
    }
}

@Composable
private fun PracticeRecordingTimeText(
    currentSeconds: Int,
    totalSeconds: Int,
    state: PracticeRecordingControlState,
) {
    if (state != PracticeRecordingControlState.PLAYING && state != PracticeRecordingControlState.PLAYBACK_PAUSED) {
        Text(
            text = state
                .displaySeconds(
                    currentSeconds = currentSeconds,
                    totalSeconds = totalSeconds,
                ).toTimerText(),
            style = PrezelTheme.typography.title1Medium,
            color = PrezelTheme.colors.textMedium,
        )
        return
    }

    val currentColor = PrezelTheme.colors.interactiveRegular
    val totalColor = PrezelTheme.colors.textSmall

    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = currentColor)) {
                append(currentSeconds.toTimerText())
            }
            withStyle(SpanStyle(color = totalColor)) {
                append("/")
                append(totalSeconds.toTimerText())
            }
        },
        style = PrezelTheme.typography.title1Medium,
    )
}

private data class PracticeRecordingControlAction(
    @param:DrawableRes val iconResId: Int,
    val colorType: PracticeRecordingControlActionColorType,
    val onClick: () -> Unit,
)

private enum class PracticeRecordingControlActionColorType {
    RECORD,
    REGULAR,
}

private fun PracticeRecordingControlState.actions(
    onStartRecording: () -> Unit,
    onPauseRecording: () -> Unit,
    onResumeRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onResetRecording: () -> Unit,
    onSelectAudioFile: () -> Unit,
    onStartPlayback: () -> Unit,
    onPausePlayback: () -> Unit,
    onResumePlayback: () -> Unit,
    onStopPlayback: () -> Unit,
): List<PracticeRecordingControlAction> =
    when (this) {
        PracticeRecordingControlState.READY_TO_RECORD -> listOf(
            PracticeRecordingControlAction(
                iconResId = PrezelIcons.Recording,
                colorType = PracticeRecordingControlActionColorType.RECORD,
                onClick = onStartRecording,
            ),
            PracticeRecordingControlAction(
                iconResId = PrezelIcons.Folder,
                colorType = PracticeRecordingControlActionColorType.REGULAR,
                onClick = onSelectAudioFile,
            ),
        )

        PracticeRecordingControlState.RECORDING -> recordingActions(
            primaryIconResId = PrezelIcons.Pause,
            onPrimaryClick = onPauseRecording,
            onStopRecording = onStopRecording,
            onResetRecording = onResetRecording,
        )

        PracticeRecordingControlState.RECORDING_PAUSED -> recordingActions(
            primaryIconResId = PrezelIcons.Play,
            onPrimaryClick = onResumeRecording,
            onStopRecording = onStopRecording,
            onResetRecording = onResetRecording,
        )

        PracticeRecordingControlState.READY_TO_PLAY -> listOf(
            PracticeRecordingControlAction(
                iconResId = PrezelIcons.Play,
                colorType = PracticeRecordingControlActionColorType.REGULAR,
                onClick = onStartPlayback,
            ),
            PracticeRecordingControlAction(
                iconResId = PrezelIcons.Folder,
                colorType = PracticeRecordingControlActionColorType.REGULAR,
                onClick = onSelectAudioFile,
            ),
        )

        PracticeRecordingControlState.PLAYING -> playbackActions(
            primaryIconResId = PrezelIcons.Pause,
            onPrimaryClick = onPausePlayback,
            onStopPlayback = onStopPlayback,
        )

        PracticeRecordingControlState.PLAYBACK_PAUSED -> playbackActions(
            primaryIconResId = PrezelIcons.Play,
            onPrimaryClick = onResumePlayback,
            onStopPlayback = onStopPlayback,
        )
    }

private fun recordingActions(
    @DrawableRes primaryIconResId: Int,
    onPrimaryClick: () -> Unit,
    onStopRecording: () -> Unit,
    onResetRecording: () -> Unit,
): List<PracticeRecordingControlAction> =
    listOf(
        PracticeRecordingControlAction(
            iconResId = primaryIconResId,
            colorType = PracticeRecordingControlActionColorType.REGULAR,
            onClick = onPrimaryClick,
        ),
        PracticeRecordingControlAction(
            iconResId = PrezelIcons.Stop,
            colorType = PracticeRecordingControlActionColorType.REGULAR,
            onClick = onStopRecording,
        ),
        PracticeRecordingControlAction(
            iconResId = PrezelIcons.Reset,
            colorType = PracticeRecordingControlActionColorType.REGULAR,
            onClick = onResetRecording,
        ),
    )

private fun playbackActions(
    @DrawableRes primaryIconResId: Int,
    onPrimaryClick: () -> Unit,
    onStopPlayback: () -> Unit,
): List<PracticeRecordingControlAction> =
    listOf(
        PracticeRecordingControlAction(
            iconResId = primaryIconResId,
            colorType = PracticeRecordingControlActionColorType.REGULAR,
            onClick = onPrimaryClick,
        ),
        PracticeRecordingControlAction(
            iconResId = PrezelIcons.Stop,
            colorType = PracticeRecordingControlActionColorType.REGULAR,
            onClick = onStopPlayback,
        ),
    )

@Composable
private fun PracticeRecordingControlAction.iconColor() =
    when (colorType) {
        PracticeRecordingControlActionColorType.RECORD -> PrezelTheme.colors.feedbackBadRegular
        PracticeRecordingControlActionColorType.REGULAR -> PrezelTheme.colors.iconRegular
    }

private fun PracticeRecordingControlState.displaySeconds(
    currentSeconds: Int,
    totalSeconds: Int,
): Int =
    when (this) {
        PracticeRecordingControlState.READY_TO_PLAY -> totalSeconds
        else -> currentSeconds
    }

private fun Int.toTimerText(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}
