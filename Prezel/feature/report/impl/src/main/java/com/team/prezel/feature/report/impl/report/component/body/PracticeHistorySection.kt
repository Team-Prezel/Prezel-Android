package com.team.prezel.feature.report.impl.report.component.body

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.PracticeCard
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.report.component.common.ReportSection
import com.team.prezel.feature.report.impl.report.model.PracticeRecordsUiModel
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@Composable
internal fun PracticeHistorySection(practiceRecords: PracticeRecordsUiModel) {
    ReportSection(
        title = {
            Text(
                text = stringResource(R.string.feature_report_impl_section_practice_history),
                style = PrezelTheme.typography.title2Bold,
                color = PrezelTheme.colors.textLarge,
            )
        },
    ) {
        PracticeCard(
            dDay = practiceRecords.endDate,
            items = practiceRecords.practices,
            showActionButton = false,
        )
    }
}

@BasicPreview
@Composable
private fun PracticeHistorySectionPreview() {
    val base = LocalDate(2026, 5, 14)

    val practiceRecords = PracticeRecordsUiModel(
        startDate = base,
        endDate = base.plus(8, DateTimeUnit.DAY),
        practicedDates = listOf(
            LocalDate(2026, 5, 14),
            LocalDate(2026, 5, 16),
            LocalDate(2026, 5, 17),
            LocalDate(2026, 5, 20),
        ),
    )

    PrezelTheme {
        PracticeHistorySection(practiceRecords = practiceRecords)
    }
}
