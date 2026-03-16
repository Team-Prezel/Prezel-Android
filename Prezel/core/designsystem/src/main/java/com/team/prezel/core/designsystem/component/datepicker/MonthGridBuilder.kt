package com.team.prezel.core.designsystem.component.datepicker

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.onDay

internal fun buildMonthGrid(
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
