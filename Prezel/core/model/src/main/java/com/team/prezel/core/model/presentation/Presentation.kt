package com.team.prezel.core.model.presentation

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

data class Presentation(
    val id: Long,
    val title: String,
    val date: LocalDate,
    val category: Category,
    val purpose: Purpose,
    val style: Style,
    val audience: Audience,
) {
    fun dDay(now: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())): Int = (date.toEpochDays() - now.toEpochDays()).toInt()
}
