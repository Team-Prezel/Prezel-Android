package com.team.prezel.core.designsystem.component.datepicker.config

import androidx.compose.runtime.Immutable
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@Immutable
internal sealed interface DayCellType {
    val date: LocalDate
    val isSelected: Boolean

    data class Default(
        override val date: LocalDate,
        override val isSelected: Boolean,
    ) : DayCellType

    data class Past(
        override val date: LocalDate,
        override val isSelected: Boolean,
    ) : DayCellType

    data class Today(
        override val date: LocalDate,
        override val isSelected: Boolean,
    ) : DayCellType

    data class Holiday(
        override val date: LocalDate,
        override val isSelected: Boolean,
    ) : DayCellType {
        companion object {
            fun isHoliday(date: LocalDate): Boolean = date.dayOfWeek == DayOfWeek.SUNDAY
        }
    }

    companion object {
        fun from(
            date: LocalDate,
            selectedDate: LocalDate,
            today: LocalDate,
        ): DayCellType {
            val isSelected = date == selectedDate

            return when {
                date < today -> Past(date = date, isSelected = isSelected)
                date == today -> Today(date = date, isSelected = isSelected)
                Holiday.isHoliday(date = date) -> Holiday(date = date, isSelected = isSelected)
                else -> Default(date = date, isSelected = isSelected)
            }
        }
    }
}
