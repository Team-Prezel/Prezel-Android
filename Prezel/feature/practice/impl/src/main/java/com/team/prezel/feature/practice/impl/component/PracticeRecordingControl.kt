package com.team.prezel.feature.practice.impl.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.audio.AudioSource
import com.team.prezel.core.designsystem.component.actions.button.PrezelIconButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun PracticeRecordingControl(
    currentSeconds: Int,
    totalSeconds: Int,
    audioSessionState: AudioSessionState,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onStartPlayback: () -> Unit,
    onStopPlayback: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val actions = audioSessionState.actions(
        onStartRecording = onStartRecording,
        onStopRecording = onStopRecording,
        onStartPlayback = onStartPlayback,
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
            audioSessionState = audioSessionState,
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
    audioSessionState: AudioSessionState,
) {
    if (audioSessionState == AudioSessionState.Idle || audioSessionState is AudioSessionState.Recording) {
        Text(
            text = currentSeconds.toTimerText(),
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

private fun AudioSessionState.actions(
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onStartPlayback: () -> Unit,
    onStopPlayback: () -> Unit,
): List<PracticeRecordingControlAction> =
    when (this) {
        AudioSessionState.Idle -> listOf(
            PracticeRecordingControlAction(
                iconResId = PrezelIcons.Recording,
                colorType = PracticeRecordingControlActionColorType.RECORD,
                onClick = onStartRecording,
            ),
        )

        is AudioSessionState.Recording -> stopAction(onStop = onStopRecording)

        is AudioSessionState.ReadyToPlay -> listOf(
            PracticeRecordingControlAction(
                iconResId = PrezelIcons.Play,
                colorType = PracticeRecordingControlActionColorType.REGULAR,
                onClick = onStartPlayback,
            ),
        )

        is AudioSessionState.Playing -> stopAction(onStop = onStopPlayback)
    }

private fun stopAction(onStop: () -> Unit): List<PracticeRecordingControlAction> =
    listOf(
        PracticeRecordingControlAction(
            iconResId = PrezelIcons.Stop,
            colorType = PracticeRecordingControlActionColorType.REGULAR,
            onClick = onStop,
        ),
    )

@Composable
private fun PracticeRecordingControlAction.iconColor() =
    when (colorType) {
        PracticeRecordingControlActionColorType.RECORD -> PrezelTheme.colors.feedbackBadRegular
        PracticeRecordingControlActionColorType.REGULAR -> PrezelTheme.colors.iconRegular
    }

private fun Int.toTimerText(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}

@BasicPreview
@Composable
private fun PracticeRecordingControlPreview() {
    PrezelTheme {
        Column(
            modifier = Modifier
                .background(PrezelTheme.colors.bgRegular)
                .padding(PrezelTheme.spacing.V20),
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V20),
        ) {
            PracticeRecordingControl(
                currentSeconds = 0,
                totalSeconds = 0,
                audioSessionState = AudioSessionState.Idle,
                onStartRecording = {},
                onStopRecording = {},
                onStartPlayback = {},
                onStopPlayback = {},
            )

            PracticeRecordingControl(
                currentSeconds = 8,
                totalSeconds = 0,
                audioSessionState = AudioSessionState.Recording(elapsedSeconds = 8),
                onStartRecording = {},
                onStopRecording = {},
                onStartPlayback = {},
                onStopPlayback = {},
            )

            PracticeRecordingControl(
                currentSeconds = 16,
                totalSeconds = 45,
                audioSessionState = AudioSessionState.ReadyToPlay(
                    source = AudioSource.RecordedFile(filePath = "preview.m4a"),
                    positionSeconds = 16,
                    durationSeconds = 45,
                ),
                onStartRecording = {},
                onStopRecording = {},
                onStartPlayback = {},
                onStopPlayback = {},
            )

            PracticeRecordingControl(
                currentSeconds = 24,
                totalSeconds = 45,
                audioSessionState = AudioSessionState.Playing(
                    source = AudioSource.RecordedFile(filePath = "preview.m4a"),
                    positionSeconds = 24,
                    durationSeconds = 45,
                ),
                onStartRecording = {},
                onStopRecording = {},
                onStartPlayback = {},
                onStopPlayback = {},
            )
        }
    }
}
