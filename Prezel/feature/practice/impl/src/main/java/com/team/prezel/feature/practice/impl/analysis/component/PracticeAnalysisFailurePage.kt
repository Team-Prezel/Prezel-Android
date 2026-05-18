package com.team.prezel.feature.practice.impl.analysis.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.StatusView
import com.team.prezel.feature.practice.impl.R
import com.team.prezel.feature.practice.impl.analysis.model.PracticeAnalysisErrorType
import com.team.prezel.core.ui.R as CoreUiR

@Composable
internal fun PracticeAnalysisFailurePage(
    errorType: PracticeAnalysisErrorType,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatusView(
        title = stringResource(R.string.feature_practice_impl_practice_recording_analysis_error_title),
        description = stringResource(R.string.feature_practice_impl_practice_recording_analysis_error_description),
        modifier = modifier,
        visual = {
            Image(
                painter = painterResource(errorType.drawableResId),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
            )
        },
        action = {
            PrezelButton(
                text = stringResource(R.string.feature_practice_impl_practice_recording_analysis_retry),
                iconResId = PrezelIcons.Reset,
                type = ButtonType.FILLED,
                size = ButtonSize.SMALL,
                hierarchy = ButtonHierarchy.SECONDARY,
                isRounded = true,
                onClick = onRetry,
            )
        },
    )
}

private val PracticeAnalysisErrorType.drawableResId: Int
    @DrawableRes
    get() = when (this) {
        PracticeAnalysisErrorType.ANALYSIS_FAILED -> CoreUiR.drawable.core_ui_error_analyze
        PracticeAnalysisErrorType.VOICE_RECOGNITION_FAILED -> CoreUiR.drawable.core_ui_error_voice
    }

@BasicPreview
@Composable
private fun PracticeAnalysisAnalyzeFailurePagePreview() {
    PrezelTheme {
        PracticeAnalysisFailurePage(
            errorType = PracticeAnalysisErrorType.ANALYSIS_FAILED,
            onRetry = {},
        )
    }
}

@BasicPreview
@Composable
private fun PracticeAnalysisVoiceFailurePagePreview() {
    PrezelTheme {
        PracticeAnalysisFailurePage(
            errorType = PracticeAnalysisErrorType.VOICE_RECOGNITION_FAILED,
            onRetry = {},
        )
    }
}
