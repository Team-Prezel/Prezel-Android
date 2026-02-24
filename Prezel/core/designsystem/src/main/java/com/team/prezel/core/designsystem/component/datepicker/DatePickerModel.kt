package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.runtime.Immutable
import java.time.DayOfWeek
import java.time.LocalDate

@Immutable
internal data class DayCell(
    val date: LocalDate,
    val isSelected: Boolean,
    val isToday: Boolean,
    val isVisible: Boolean,
) {
    val dayText: String = date.dayOfMonth.toString()
    val isSunday: Boolean = date.dayOfWeek == DayOfWeek.SUNDAY
}
