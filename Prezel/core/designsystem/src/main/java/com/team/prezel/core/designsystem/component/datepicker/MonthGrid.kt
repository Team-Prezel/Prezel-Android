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
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.onDay

@Composable
internal fun MonthGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    today: LocalDate,
    onSelect: (LocalDate) -> Unit,
) {
    val visibleWeeks = remember(yearMonth, today) {
        buildMonthGrid(
            yearMonth = yearMonth,
            firstDayOfWeek = DayOfWeek.SUNDAY,
        ).chunked(7)
            .filter { week -> week.hasVisibleDate(today) }
    }

    Column(modifier = Modifier.padding(top = PrezelTheme.spacing.V16)) {
        visibleWeeks.forEach { week ->
            WeekRow(
                week = week,
                selectedDate = selectedDate,
                today = today,
                onSelect = onSelect,
            )
        }
    }
}

@Composable
private fun WeekRow(
    week: List<LocalDate?>,
    selectedDate: LocalDate?,
    today: LocalDate,
    onSelect: (LocalDate) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        week.forEach { date ->
            if (date == null) {
                DayCellView(uiModel = null) { }
                return@forEach
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

private fun buildMonthGrid(
    yearMonth: YearMonth,
    firstDayOfWeek: DayOfWeek,
): ImmutableList<LocalDate?> {
    val firstOfMonth = yearMonth.onDay(1)
    val lastDay = yearMonth.numberOfDays

    val shift = ((firstOfMonth.dayOfWeek.isoDayNumber - firstDayOfWeek.isoDayNumber) + 7) % 7
    val totalCells = 42

    return (0 until totalCells)
        .map { index ->
            val dayNumber = index - shift + 1
            if (dayNumber in 1..lastDay) {
                yearMonth.onDay(dayNumber)
            } else {
                null
            }
        }.toPersistentList()
}

private fun List<LocalDate?>.hasVisibleDate(today: LocalDate): Boolean = any { date -> date != null && date >= today }

@ThemePreview
@Composable
private fun MonthGridPreview() {
    PrezelTheme {
        MonthGrid(
            yearMonth = YearMonth(year = 2026, month = 3),
            selectedDate = LocalDate(year = 2026, month = 3, day = 22),
            today = LocalDate(year = 2026, month = 3, day = 16),
            onSelect = {},
        )
    }
}
