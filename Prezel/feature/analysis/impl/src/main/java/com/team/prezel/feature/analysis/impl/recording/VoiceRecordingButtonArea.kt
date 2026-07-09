package com.team.prezel.feature.analysis.impl.recording

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.PrezelIconButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.analysis.impl.R

@Composable
internal fun VoiceRecordingButtonArea(
    recordingState: AudioSessionState,
    analyzeEnabled: Boolean,
    showDivider: Boolean,
    onClickRecordingControl: () -> Unit,
    onStopRecording: () -> Unit,
    onResetRecording: () -> Unit,
    onAnalyze: () -> Unit,
) {
    when (recordingState) {
        AudioSessionState.Idle -> IdleRecordingButtonArea(
            recordingState = recordingState,
            showDivider = showDivider,
            onClickRecordingControl = onClickRecordingControl,
        )

        is AudioSessionState.Recording,
        is AudioSessionState.PausedRecording,
        -> ActiveRecordingButtonArea(
            recordingState = recordingState,
            showDivider = showDivider,
            onClickRecordingControl = onClickRecordingControl,
            onStopRecording = onStopRecording,
        )

        is AudioSessionState.ReadyToPlay,
        is AudioSessionState.Playing,
        -> CompletedRecordingButtonArea(
            analyzeEnabled = analyzeEnabled,
            showDivider = showDivider,
            onResetRecording = onResetRecording,
            onAnalyze = onAnalyze,
        )
    }
}

@Composable
private fun IdleRecordingButtonArea(
    recordingState: AudioSessionState,
    showDivider: Boolean,
    onClickRecordingControl: () -> Unit,
) {
    PrezelButtonArea(
        showBackground = showDivider,
        mainButton = { buttonModifier ->
            RecordingIconButton(
                iconResId = recordingState.actionIconResId,
                iconColor = recordingState.actionIconColor,
                modifier = buttonModifier,
                onClick = onClickRecordingControl,
            )
        },
    )
}

@Composable
private fun ActiveRecordingButtonArea(
    recordingState: AudioSessionState,
    showDivider: Boolean,
    onClickRecordingControl: () -> Unit,
    onStopRecording: () -> Unit,
) {
    PrezelButtonArea(
        showBackground = showDivider,
        isVertical = false,
        isStrongStrength = false,
        mainButton = { buttonModifier ->
            RecordingIconButton(
                iconResId = PrezelIcons.Stop,
                iconColor = PrezelTheme.colors.iconRegular,
                modifier = buttonModifier,
                onClick = onStopRecording,
            )
        },
        subButton = { buttonModifier ->
            RecordingIconButton(
                iconResId = recordingState.actionIconResId,
                iconColor = recordingState.actionIconColor,
                modifier = buttonModifier,
                onClick = onClickRecordingControl,
            )
        },
    )
}

@Composable
private fun CompletedRecordingButtonArea(
    analyzeEnabled: Boolean,
    showDivider: Boolean,
    onResetRecording: () -> Unit,
    onAnalyze: () -> Unit,
) {
    PrezelButtonArea(
        showBackground = showDivider,
        isVertical = false,
        mainButton = { buttonModifier ->
            PrezelButton(
                text = stringResource(R.string.feature_analysis_impl_analyze),
                modifier = buttonModifier,
                enabled = analyzeEnabled,
                type = ButtonType.FILLED,
                hierarchy = ButtonHierarchy.PRIMARY,
                onClick = onAnalyze,
            )
        },
        subButton = { buttonModifier ->
            RecordingResetButton(
                iconResId = PrezelIcons.Reset,
                iconColor = PrezelTheme.colors.iconRegular,
                modifier = buttonModifier.width(52.dp),
                onClick = onResetRecording,
            )
        },
    )
}

@Composable
private fun RecordingResetButton(
    @DrawableRes iconResId: Int,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    PrezelIconButton(
        iconResId = iconResId,
        modifier = modifier.height(48.dp),
        buttonDefault = PrezelButtonDefaults.getDefault(
            isIconOnly = true,
            isRounded = false,
            type = ButtonType.GHOST,
            size = ButtonSize.REGULAR,
            hierarchy = ButtonHierarchy.SECONDARY,
            contentColor = iconColor,
            backgroundColor = Color.Transparent,
            iconSize = 20.dp,
        ),
        onClick = onClick,
    )
}

@Composable
private fun RecordingIconButton(
    @DrawableRes iconResId: Int,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    PrezelIconButton(
        iconResId = iconResId,
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(PrezelTheme.radius.V8)),
        buttonDefault = PrezelButtonDefaults.getDefault(
            isIconOnly = true,
            type = ButtonType.FILLED,
            size = ButtonSize.REGULAR,
            hierarchy = ButtonHierarchy.SECONDARY,
            isRounded = false,
            contentColor = iconColor,
            backgroundColor = PrezelTheme.colors.bgLarge,
            iconSize = 20.dp,
        ),
        onClick = onClick,
    )
}
