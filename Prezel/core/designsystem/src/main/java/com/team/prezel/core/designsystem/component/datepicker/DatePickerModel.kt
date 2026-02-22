package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.runtime.Immutable
import java.time.DayOfWeek
import java.time.LocalDate

internal data class DayCell(
    val date: LocalDate?,
    val isInMonth: Boolean,
)

@Immutable
internal data class DayCellUiModel(
    val date: LocalDate,
    val text: String,
    val isSelected: Boolean,
    val isToday: Boolean,
    val isSunday: Boolean,
    val isInMonth: Boolean,
    val enabled: Boolean,
)

internal fun DayCell.toUiModel(
    selectedDate: LocalDate?,
    today: LocalDate,
    enabled: Boolean = isInMonth,
): DayCellUiModel? {
    if (date == null) return null

    return DayCellUiModel(
        date = date,
        text = date.dayOfMonth.toString(),
        isSelected = date == selectedDate,
        isToday = date == today,
        isSunday = date.dayOfWeek == DayOfWeek.SUNDAY,
        isInMonth = isInMonth,
        enabled = enabled && isInMonth,
    )
}
