package com.team.prezel.core.designsystem.component.datepicker.config

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.onDay

@Composable
internal fun DatePickerMonth(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    today: LocalDate,
    onSelect: (LocalDate) -> Unit,
    config: DatePickerDefault,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(PrezelTheme.spacing.V20)) {
        MonthSectionHeader(yearMonth = yearMonth)

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))

        MonthGrid(
            yearMonth = yearMonth,
            selectedDate = selectedDate,
            today = today,
            onSelect = onSelect,
            config = config,
        )
    }
}

@Composable
private fun MonthSectionHeader(
    yearMonth: YearMonth,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(
            id = R.string.core_designsystem_date_picker_month_title,
            yearMonth.year,
            yearMonth.month.number,
        ),
        color = PrezelTheme.colors.textLarge,
        style = PrezelTheme.typography.body3Medium,
        modifier = modifier,
    )
}

@Composable
private fun MonthGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate,
    today: LocalDate,
    onSelect: (LocalDate) -> Unit,
    config: DatePickerDefault,
    modifier: Modifier = Modifier,
) {
    val dates = remember(yearMonth) { buildMonthGrid(yearMonth) }
    val weeks = remember(dates, selectedDate, today) {
        dates
            .map { date ->
                if (date == null) return@map null
                DayCellType.from(date = date, selectedDate = selectedDate, today = today)
            }.chunked(7)
            .filter { week -> week.any { cell -> cell != null && cell !is DayCellType.Past } }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V2),
    ) {
        weeks.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { cell ->
                    DayCell(
                        dayCell = cell,
                        config = config,
                        onClick = { cell?.let { onSelect(it.date) } },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

private fun buildMonthGrid(yearMonth: YearMonth): List<LocalDate?> {
    val firstDate = yearMonth.onDay(1)
    val lastDay = yearMonth.numberOfDays
    val leadingEmptyCount = firstDate.dayOfWeek.isoDayNumber % 7

    return (0 until 42)
        .map { index ->
            val dayNumber = index - leadingEmptyCount + 1
            if (dayNumber !in 1..lastDay) return@map null

            yearMonth.onDay(dayNumber)
        }
}

@BasicPreview
@Composable
private fun DatePickerMonthPreview() {
    PreviewSection(
        title = "Month Section",
        description = "DatePicker에 사용되는 리소스입니다.",
    ) {
        DatePickerMonth(
            yearMonth = YearMonth(year = 2026, month = 2),
            selectedDate = LocalDate(year = 2026, month = 2, day = 28),
            today = LocalDate(year = 2026, month = 2, day = 18),
            onSelect = {},
            config = DatePickerDefaults.default(),
        )
    }
}
