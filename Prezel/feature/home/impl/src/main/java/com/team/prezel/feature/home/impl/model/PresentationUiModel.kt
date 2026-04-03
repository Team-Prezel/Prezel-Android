package com.team.prezel.feature.home.impl.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Immutable
internal data class PresentationUiModel(
    val id: String,
    val category: CategoryUiModel,
    val title: String,
    val date: LocalDate,
) {
    fun dDay(now: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())): Int = (date.toEpochDays() - now.toEpochDays()).toInt()
}
