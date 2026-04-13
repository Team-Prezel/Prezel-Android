package com.team.prezel.core.model.presentation

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

data class Presentation(
    val id: Long,
    val category: Category,
    val title: String,
    val date: LocalDate,
) {
    fun dDay(now: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())): Int = (date.toEpochDays() - now.toEpochDays()).toInt()
}
