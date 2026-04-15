package com.team.prezel.feature.history.impl.mapper

import com.team.prezel.core.model.presentation.HistoryPresentation
import com.team.prezel.feature.history.impl.model.HistoryUiModel

internal fun HistoryPresentation.toUiModel(): HistoryUiModel {
    val dDay = dDay()
    return HistoryUiModel(
        id = id,
        dDay = dDay,
        date = date,
        title = title,
        category = category,
        purpose = purpose,
        style = style,
        audience = audience,
    )
}
