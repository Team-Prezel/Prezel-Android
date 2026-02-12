package com.team.prezel.core.designsystem.component.datepicker

import java.time.DayOfWeek
import java.time.LocalDate

data class DayCell(
    val date: LocalDate?,
    val isInMonth: Boolean,
)

data class DayCellUiModel(
    val date: LocalDate,
    val text: String,
    val isSelected: Boolean,
    val isToday: Boolean,
    val isSunday: Boolean,
    val enabled: Boolean,
)

internal fun DayCell.toUiModel(
    selectedDate: LocalDate?,
    today: LocalDate,
    enabled: Boolean = true,
): DayCellUiModel? {
    val d = date ?: return null
    return DayCellUiModel(
        date = d,
        text = d.dayOfMonth.toString(),
        isSelected = d == selectedDate,
        isToday = d == today,
        isSunday = d.dayOfWeek == DayOfWeek.SUNDAY,
        enabled = enabled,
    )
}
