package com.team.prezel.feature.history.impl.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.PresentationInfo
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
    val dDayLabel: String = when {
        dDay > 0 -> "D-$dDay"
        dDay == 0 -> "D-Day"
        else -> "D+${-dDay}"
    }

    val dateLabel: String = "%04d.%02d.%02d".format(date.year, date.month.number, date.day)

    companion object {
        fun PresentationInfo.toUiModel(): HistoryUiModel =
            HistoryUiModel(
                id = id,
                dDay = dDay.toIntOrNull() ?: -1,
                date = presentationDate,
                title = title,
                category = category,
                purpose = purpose,
                style = style,
                audience = audience,
            )
    }
}
