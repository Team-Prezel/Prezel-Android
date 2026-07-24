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
            private val holidays =
                setOf(
                    LocalDate(year = 2026, month = 8, day = 15),
                    LocalDate(year = 2026, month = 8, day = 17),
                    LocalDate(year = 2026, month = 9, day = 24),
                    LocalDate(year = 2026, month = 9, day = 25),
                    LocalDate(year = 2026, month = 9, day = 26),
                    LocalDate(year = 2026, month = 10, day = 3),
                    LocalDate(year = 2026, month = 10, day = 5),
                    LocalDate(year = 2026, month = 10, day = 9),
                    LocalDate(year = 2026, month = 12, day = 25),
                )

            fun isHoliday(date: LocalDate): Boolean = date.dayOfWeek == DayOfWeek.SUNDAY || date in holidays
        }
    }

    companion object {
        fun from(
            date: LocalDate,
            selectedDate: LocalDate?,
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
