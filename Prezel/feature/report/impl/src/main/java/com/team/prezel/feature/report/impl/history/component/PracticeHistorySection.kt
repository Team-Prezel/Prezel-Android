package com.team.prezel.feature.report.impl.history.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.PracticeCard
import com.team.prezel.core.ui.component.PracticeCardItem
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.detail.component.ReportSection
import com.team.prezel.feature.report.impl.detail.model.PracticeUiModel
import com.team.prezel.feature.report.impl.detail.preview.ReportDetailPreviewData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate

@Composable
internal fun PracticeHistorySection(
    presentationDate: LocalDate,
    practices: ImmutableList<PracticeUiModel>,
) {
    ReportSection(
        title = {
            Text(
                text = stringResource(R.string.feature_report_impl_section_practice_history),
                style = PrezelTheme.typography.body2Bold,
                color = PrezelTheme.colors.textLarge,
            )
        },
    ) {
        if (practices.isEmpty()) {
            Text(
                text = stringResource(R.string.feature_report_impl_empty_practice_history),
                style = PrezelTheme.typography.body2Regular,
                color = PrezelTheme.colors.textMedium,
            )
        } else {
            PracticeCard(
                dDay = presentationDate,
                items = practices
                    .map { item ->
                        PracticeCardItem(
                            date = item.date,
                            isPracticed = item.isPracticed,
                        )
                    }.toImmutableList(),
                showActionButton = false,
            )
        }
    }
}

@BasicPreview
@Composable
private fun PracticeHistorySectionPreview() {
    PrezelTheme {
        PracticeHistorySection(
            presentationDate = LocalDate(2026, 5, 14),
            practices = ReportDetailPreviewData.historyState.practices,
        )
    }
}

@BasicPreview
@Composable
private fun EmptyPracticeHistorySectionPreview() {
    PrezelTheme {
        PracticeHistorySection(
            presentationDate = LocalDate(2026, 5, 14),
            practices = persistentListOf(),
        )
    }
}
