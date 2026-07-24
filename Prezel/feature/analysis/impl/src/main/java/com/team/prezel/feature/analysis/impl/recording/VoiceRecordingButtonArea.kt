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
    modifier: Modifier = Modifier,
    onClickRecordingControl: () -> Unit,
    onStopRecording: () -> Unit,
    onResetRecording: () -> Unit,
    onAnalyze: () -> Unit,
) {
    when (recordingState) {
        AudioSessionState.Idle -> IdleRecordingButtonArea(
            recordingState = recordingState,
            modifier = modifier,
            onClickRecordingControl = onClickRecordingControl,
        )

        is AudioSessionState.Recording,
        is AudioSessionState.PausedRecording,
        -> ActiveRecordingButtonArea(
            recordingState = recordingState,
            modifier = modifier,
            onClickRecordingControl = onClickRecordingControl,
            onStopRecording = onStopRecording,
        )

        is AudioSessionState.ReadyToPlay,
        is AudioSessionState.Playing,
        -> CompletedRecordingButtonArea(
            analyzeEnabled = analyzeEnabled,
            modifier = modifier,
            onResetRecording = onResetRecording,
            onAnalyze = onAnalyze,
        )
    }
}

@Composable
private fun IdleRecordingButtonArea(
    recordingState: AudioSessionState,
    modifier: Modifier,
    onClickRecordingControl: () -> Unit,
) {
    PrezelButtonArea(
        modifier = modifier,
        showBackground = true,
        showDivider = false,
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
    modifier: Modifier,
    onClickRecordingControl: () -> Unit,
    onStopRecording: () -> Unit,
) {
    PrezelButtonArea(
        modifier = modifier,
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
    modifier: Modifier,
    onResetRecording: () -> Unit,
    onAnalyze: () -> Unit,
) {
    PrezelButtonArea(
        modifier = modifier,
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
                modifier = buttonModifier.width(80.dp),
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
            iconSize = 24.dp,
        ),
        onClick = onClick,
    )
}
