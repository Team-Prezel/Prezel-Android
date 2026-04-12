package com.team.prezel.feature.home.impl.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Presentation
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
    val dDay: Int,
    val practiceCount: Int = 0,
) {
    val isPastPresentation: Boolean = dDay < 0

    val dDayLabel: String = when (dDay) {
        0 -> "D-Day"
        in Int.MIN_VALUE..-1 -> "D+${-dDay}"
        else -> "D-$dDay"
    }

    companion object {
        fun Presentation.toUiModel(now: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())): PresentationUiModel =
            PresentationUiModel(
                id = id,
                category = category,
                title = title,
                date = date,
                dDay = dDay(now = now),
            )
    }
}
