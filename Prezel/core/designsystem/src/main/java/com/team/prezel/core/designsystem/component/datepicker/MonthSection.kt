package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
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
            text = stringResource(
                id = R.string.core_designsystem_date_picker_month_title,
                yearMonth.year,
                yearMonth.month.number,
            ),
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

@BasicPreview
@Composable
private fun MonthSectionPreview() {
    PreviewSection(
        title = "Month Section",
        description = "DatePicker에 사용되는 리소스입니다.",
    ) {
        MonthSection(
            yearMonth = YearMonth(year = 2026, month = 2),
            selectedDate = LocalDate(year = 2026, month = 2, day = 26),
            today = LocalDate(year = 2026, month = 2, day = 25),
            onSelect = {},
        )
    }
}
