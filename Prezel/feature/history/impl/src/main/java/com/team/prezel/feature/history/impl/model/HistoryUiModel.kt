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
    val presentationId: Long,
    val title: String,
    val presentationDate: LocalDate,
    val category: Category,
    val purpose: Purpose,
    val style: Style,
    val audience: Audience,
    val dDay: String,
) {
    val dateLabel: String = "%04d.%02d.%02d".format(presentationDate.year, presentationDate.month.number, presentationDate.day)

    companion object {
        fun PresentationInfo.toUiModel(): HistoryUiModel =
            HistoryUiModel(
                presentationId = id,
                title = title,
                presentationDate = presentationDate,
                category = category,
                purpose = purpose,
                style = style,
                audience = audience,
                dDay = dDay,
            )
    }
}
