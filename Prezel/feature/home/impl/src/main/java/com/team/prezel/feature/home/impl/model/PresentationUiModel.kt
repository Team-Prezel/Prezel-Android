package com.team.prezel.feature.home.impl.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.Category
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Immutable
internal data class PresentationUiModel(
    val id: Long,
    val category: Category,
    val title: String,
    val date: LocalDate,
) {
    fun dDay(now: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())): Int = (date.toEpochDays() - now.toEpochDays()).toInt()
}
