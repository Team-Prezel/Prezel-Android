package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number

@Composable
internal fun MonthSection(
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    today: LocalDate,
    onSelect: (LocalDate) -> Unit,
) {
    Column(
        modifier = Modifier.padding(PrezelTheme.spacing.V20),
    ) {
        Text(
            text = "${yearMonth.year}년 ${yearMonth.month.number}월",
            color = PrezelTheme.colors.textLarge,
            style = PrezelTheme.typography.body3Medium,
        )

        MonthGrid(
            yearMonth = yearMonth,
            selectedDate = selectedDate,
            today = today,
            onSelect = onSelect,
        )
    }
}

@ThemePreview
@Composable
private fun MonthSectionPreview() {
    PrezelTheme {
        MonthSection(
            yearMonth = YearMonth(year = 2026, month = 2),
            selectedDate = LocalDate(year = 2026, month = 2, day = 26),
            today = LocalDate(year = 2026, month = 2, day = 25),
            onSelect = {},
        )
    }
}
