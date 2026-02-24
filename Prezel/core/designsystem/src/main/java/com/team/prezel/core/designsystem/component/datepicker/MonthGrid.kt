package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.ImmutableList
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun MonthGrid(
    month: YearMonth,
    selectedDate: LocalDate?,
    today: LocalDate,
    onSelect: (LocalDate) -> Unit,
) {
    val (cells, lastWeek) = remember(month) {
        val c = buildMonthGrid(month, firstDayOfWeek = DayOfWeek.SUNDAY)
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

            val isPast = date.isBefore(today)
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

@ThemePreview
@Composable
private fun MonthGridPreview() {
    PrezelTheme {
        MonthGrid(
            month = YearMonth.of(2026, 2),
            selectedDate = LocalDate.of(2026, 2, 26),
            today = LocalDate.of(2026, 2, 25),
            onSelect = {},
        )
    }
}
