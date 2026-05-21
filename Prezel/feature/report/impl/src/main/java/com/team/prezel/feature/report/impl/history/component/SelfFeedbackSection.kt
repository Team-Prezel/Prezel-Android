package com.team.prezel.feature.report.impl.history.component

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
import com.team.prezel.feature.report.impl.detail.component.ReportSection
import com.team.prezel.feature.report.impl.detail.preview.ReportDetailPreviewData

@Composable
internal fun SelfFeedbackSection(selfFeedback: String?) {
    ReportSection(
        title = {
            Text(
                text = stringResource(R.string.feature_report_impl_section_self_feedback),
                style = PrezelTheme.typography.body2Bold,
                color = PrezelTheme.colors.textLarge,
            )
        },
    ) {
        if (selfFeedback.isNullOrBlank()) {
            PrezelTextButton(
                text = "피드백 작성하기",
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
            selfFeedback = ReportDetailPreviewData.historyState.selfFeedback,
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
