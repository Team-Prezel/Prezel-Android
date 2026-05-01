package com.team.prezel.feature.home.impl.practice.component

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
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingState

internal enum class PracticeRecordingControlState {
    READY_TO_RECORD,
    RECORDING,
    READY_TO_PLAY,
    PLAYING,
}

internal fun PracticeRecordingState.toControlState(): PracticeRecordingControlState =
    when (this) {
        PracticeRecordingState.Idle -> PracticeRecordingControlState.READY_TO_RECORD
        is PracticeRecordingState.Recording -> PracticeRecordingControlState.RECORDING
        is PracticeRecordingState.Recorded -> PracticeRecordingControlState.READY_TO_PLAY
        is PracticeRecordingState.Playing -> PracticeRecordingControlState.PLAYING
    }

@Composable
internal fun PracticeRecordingControl(
    currentSeconds: Int,
    totalSeconds: Int,
    state: PracticeRecordingControlState,
    onClickControl: () -> Unit,
    modifier: Modifier = Modifier,
) {
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

        PrezelIconButton(
            iconResId = state.iconResId,
            modifier = Modifier.size(48.dp),
            isRounded = true,
            buttonDefault = PrezelButtonDefaults.getDefault(
                isIconOnly = true,
                isRounded = true,
                type = ButtonType.FILLED,
                size = ButtonSize.REGULAR,
                hierarchy = ButtonHierarchy.SECONDARY,
                contentColor = state.iconColor(),
                backgroundColor = PrezelTheme.colors.bgLarge,
                iconSize = 20.dp,
            ),
            onClick = onClickControl,
        )
    }
}

@Composable
private fun PracticeRecordingTimeText(
    currentSeconds: Int,
    totalSeconds: Int,
    state: PracticeRecordingControlState,
) {
    if (state != PracticeRecordingControlState.PLAYING) {
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

private val PracticeRecordingControlState.iconResId: Int
    get() = when (this) {
        PracticeRecordingControlState.READY_TO_RECORD -> PrezelIcons.Recording
        PracticeRecordingControlState.RECORDING -> PrezelIcons.Stop
        PracticeRecordingControlState.READY_TO_PLAY -> PrezelIcons.Play
        PracticeRecordingControlState.PLAYING -> PrezelIcons.Stop
    }

@Composable
private fun PracticeRecordingControlState.iconColor() =
    when (this) {
        PracticeRecordingControlState.READY_TO_RECORD -> PrezelTheme.colors.feedbackBadRegular
        PracticeRecordingControlState.RECORDING -> PrezelTheme.colors.iconRegular
        PracticeRecordingControlState.READY_TO_PLAY -> PrezelTheme.colors.iconRegular
        PracticeRecordingControlState.PLAYING -> PrezelTheme.colors.iconRegular
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
