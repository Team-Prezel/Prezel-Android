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
    val dDayLabel: String,
    val isPastPresentation: Boolean,
) {
    companion object {
        fun create(
            id: Long,
            category: Category,
            title: String,
            date: LocalDate,
            now: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
        ): PresentationUiModel {
            val dDay = (date.toEpochDays() - now.toEpochDays()).toInt()

            return PresentationUiModel(
                id = id,
                category = category,
                title = title,
                date = date,
                dDayLabel = dDay.toDdayLabel(),
                isPastPresentation = dDay < 0,
            )
        }
    }
}

private fun Int.toDdayLabel(): String =
    when (this) {
        0 -> "D-Day"
        in Int.MIN_VALUE..-1 -> "D+${-this}"
        else -> "D-$this"
    }
