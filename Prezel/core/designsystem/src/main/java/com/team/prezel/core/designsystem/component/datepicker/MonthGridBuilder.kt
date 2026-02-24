package com.team.prezel.core.designsystem.component.datepicker

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

internal fun buildMonthGrid(
    month: YearMonth,
    firstDayOfWeek: DayOfWeek,
): ImmutableList<LocalDate?> {
    val firstOfMonth = month.atDay(1)
    val lastDay = month.lengthOfMonth()

    val shift = ((firstOfMonth.dayOfWeek.value - firstDayOfWeek.value) + 7) % 7
    val totalCells = 42

    return (0 until totalCells)
        .map { index ->
            val dayNumber = index - shift + 1
            if (dayNumber in 1..lastDay) {
                month.atDay(dayNumber)
            } else {
                null
            }
        }.toPersistentList()
}

internal fun lastWeekIndexToRender(cells: List<LocalDate?>): Int {
    // 마지막으로 실제 날짜가 존재하는 셀 인덱스 (0..41)
    val last = cells.indexOfLast { it != null }
    // month가 비정상일 경우 방어
    if (last < 0) return 0

    // 주 단위로 올림 → 마지막 날짜가 포함된 주 index (0..5)
    return (last / 7).coerceIn(0, 5)
}
