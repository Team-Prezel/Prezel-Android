package com.team.prezel.feature.report.impl.report.component.body

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.report.component.common.ReportSection
import com.team.prezel.feature.report.impl.report.preview.ReportPreviewUpcomingUiState

@Composable
internal fun SummarySection(summary: String) {
    ReportSection(
        title = {
            Text(
                text = stringResource(R.string.feature_report_impl_section_summary_feedback),
                style = PrezelTheme.typography.title2Bold,
                color = PrezelTheme.colors.textLarge,
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(PrezelTheme.shapes.V8)
                .background(color = PrezelTheme.colors.bgMedium)
                .padding(PrezelTheme.spacing.V12),
        ) {
            Text(
                text = summary,
                style = PrezelTheme.typography.body2Regular,
                color = PrezelTheme.colors.textMedium,
            )
        }
    }
}

@BasicPreview
@Composable
private fun SummarySectionPreview() {
    PrezelTheme {
        SummarySection(summary = ReportPreviewUpcomingUiState.summaryFeedback)
    }
}
