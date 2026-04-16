package com.team.prezel.feature.history.impl.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.HistoryPresentation
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

@Immutable
internal data class HistoryUiModel(
    val id: Long,
    val dDay: Int,
    val date: LocalDate,
    val title: String,
    val category: Category,
    val purpose: Purpose,
    val style: Style,
    val audience: Audience,
) {
    val isPreparing: Boolean = dDay >= 0

    val dDayLabel: String = when {
        dDay > 0 -> "D-$dDay"
        dDay == 0 -> "D-Day"
        else -> "D+${-dDay}"
    }

    val dateLabel: String = "%04d.%02d.%02d".format(date.year, date.month.number, date.day)

    companion object {
        fun toUiModel(historyPresentation: HistoryPresentation): HistoryUiModel {
            val dDay = historyPresentation.dDay()
            return HistoryUiModel(
                id = historyPresentation.id,
                dDay = dDay,
                date = historyPresentation.date,
                title = historyPresentation.title,
                category = historyPresentation.category,
                purpose = historyPresentation.purpose,
                style = historyPresentation.style,
                audience = historyPresentation.audience,
            )
        }
    }
}
