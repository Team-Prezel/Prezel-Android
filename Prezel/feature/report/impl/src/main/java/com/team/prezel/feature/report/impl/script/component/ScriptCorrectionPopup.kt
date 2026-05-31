package com.team.prezel.feature.report.impl.script.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.base.PrezelDropShadowDefaults
import com.team.prezel.core.designsystem.component.base.prezelDropShadow
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.ScriptErrorType
import com.team.prezel.core.ui.util.noRippleClickable
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.script.model.ScriptCorrectionUiModel

@Composable
internal fun ScriptCorrectionPopup(
    correction: ScriptCorrectionUiModel,
    onDismiss: () -> Unit,
    onApplyCorrection: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val popupRadius = PrezelTheme.radius.V8

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(PrezelTheme.spacing.V20)
            .prezelDropShadow(
                style = PrezelDropShadowDefaults.Custom(
                    borderRadius = popupRadius,
                    backgroundColor = PrezelTheme.colors.bgRegular,
                    token = PrezelDropShadowDefaults.PrezelShadowToken(
                        offsetX = 0.dp,
                        offsetY = 8.dp,
                        blurRadius = popupRadius,
                        spreadRadius = 0.dp,
                        color = PrezelTheme.colors.solidBlack.copy(alpha = 0.12f),
                    ),
                ),
            ).background(
                color = PrezelTheme.colors.bgRegular,
                shape = RoundedCornerShape(popupRadius),
            ),
    ) {
        PopupContent(correction = correction)

        PopupActions(onDismiss = onDismiss, onApplyCorrection = onApplyCorrection)
    }
}

@Composable
private fun PopupContent(
    correction: ScriptCorrectionUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(PrezelTheme.spacing.V16),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
    ) {
        Text(
            text = correction.correctedText,
            style = PrezelTheme.typography.body2Medium,
            color = PrezelTheme.colors.interactiveRegular,
        )
        Text(
            text = correction.reason,
            style = PrezelTheme.typography.caption1Regular,
            color = PrezelTheme.colors.textRegular,
        )
    }
}

@Composable
private fun PopupActions(
    onDismiss: () -> Unit,
    onApplyCorrection: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = PrezelTheme.spacing.V16, vertical = PrezelTheme.spacing.V8),
        horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16, Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PopupActionButton(
            text = stringResource(R.string.feature_report_impl_dialog_cancel_action),
            textColor = PrezelTheme.colors.textDisabled,
            onClick = onDismiss,
        )

        PopupActionButton(
            text = stringResource(R.string.feature_report_impl_script_apply),
            textColor = PrezelTheme.colors.textMedium,
            onClick = onApplyCorrection,
        )
    }
}

@Composable
private fun PopupActionButton(
    text: String,
    textColor: Color,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        style = PrezelTheme.typography.body3Medium,
        color = textColor,
        modifier = Modifier
            .padding(vertical = PrezelTheme.spacing.V8, horizontal = PrezelTheme.spacing.V12)
            .noRippleClickable(onClick = onClick),
    )
}

@BasicPreview
@Composable
private fun ScriptCorrectionPopupPreview() {
    PrezelTheme {
        ScriptCorrectionPopup(
            correction = ScriptCorrectionUiModel(
                id = 0L,
                errorType = ScriptErrorType.SPELL,
                sentence = "안녕하세요 자신의 말로 자신있게 세상을 설득할 수 있는 날을 기달리는 팀 손가락입니다.",
                originalText = "기달리는",
                correctedText = "기다리는",
                reason = "표준어는 '기다리다'를 활용한 표현이에요.",
                originalRange = 30 until 34,
            ),
            onDismiss = {},
            onApplyCorrection = {},
        )
    }
}
