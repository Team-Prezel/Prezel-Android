package com.team.prezel.feature.report.impl.component.body

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.actions.button.PrezelTextButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.component.common.ReportSection
import com.team.prezel.feature.report.impl.preview.ReportPreviewPastUiState

@Composable
internal fun SelfFeedbackSection(selfFeedback: String?) {
    ReportSection(
        title = {
            Text(
                text = stringResource(R.string.feature_report_impl_section_self_feedback),
                style = PrezelTheme.typography.title2Bold,
                color = PrezelTheme.colors.textLarge,
            )
        },
    ) {
        if (selfFeedback.isNullOrBlank()) {
            PrezelTextButton(
                text = stringResource(R.string.feature_report_impl_write_feedback),
                size = ButtonSize.REGULAR,
                type = ButtonType.FILLED,
                hierarchy = ButtonHierarchy.SECONDARY,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(PrezelTheme.shapes.V8)
                    .background(color = PrezelTheme.colors.bgMedium)
                    .padding(PrezelTheme.spacing.V12),
            ) {
                Text(
                    text = selfFeedback,
                    style = PrezelTheme.typography.body2Regular,
                    color = PrezelTheme.colors.textMedium,
                )
            }
        }
    }
}

@BasicPreview
@Composable
private fun SelfFeedbackSectionPreview() {
    PrezelTheme {
        SelfFeedbackSection(
            selfFeedback = ReportPreviewPastUiState.selfFeedback,
        )
    }
}

@BasicPreview
@Composable
private fun EmptySelfFeedbackSectionPreview() {
    PrezelTheme {
        SelfFeedbackSection(
            selfFeedback = null,
        )
    }
}
