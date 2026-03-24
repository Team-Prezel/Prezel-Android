package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

@Composable
internal fun MonthGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    today: LocalDate,
    onSelect: (LocalDate) -> Unit,
) {
    val (cells, lastWeek) = remember(yearMonth) {
        val c = buildMonthGrid(yearMonth = yearMonth, firstDayOfWeek = DayOfWeek.SUNDAY)
        c to lastWeekIndexToRender(c)
    }

    Column(modifier = Modifier.padding(top = PrezelTheme.spacing.V16)) {
        for (week in 0..lastWeek) {
            WeekRow(cells = cells, week = week, selectedDate = selectedDate, today = today, onSelect = onSelect)
        }
    }
}

@Composable
private fun WeekRow(
    cells: ImmutableList<LocalDate?>,
    week: Int,
    selectedDate: LocalDate?,
    today: LocalDate,
    onSelect: (LocalDate) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (day in 0 until 7) {
            val date = cells[week * 7 + day]

            if (date == null) {
                DayCellView(
                    uiModel = null,
                ) { }
                continue
            }

            val isPast = date < today
            val uiModel = DayCell(
                date = date,
                isSelected = date == selectedDate,
                isToday = date == today,
                isVisible = !isPast,
            )

            DayCellView(
                uiModel = uiModel,
                onClick = { onSelect(date) },
            )
        }
    }
}

@BasicPreview
@Composable
private fun MonthGridPreview() {
    PreviewSection(
        title = "DatePicker/MonthGrid",
        description = "DatePicker에 사용되는 리소스입니다.",
    ) {
        MonthGrid(
            yearMonth = YearMonth(year = 2026, month = 2),
            selectedDate = LocalDate(year = 2026, month = 2, day = 26),
            today = LocalDate(year = 2026, month = 2, day = 25),
            onSelect = {},
        )
    }
}
