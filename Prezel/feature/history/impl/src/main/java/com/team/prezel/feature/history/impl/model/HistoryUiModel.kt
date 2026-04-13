package com.team.prezel.feature.history.impl.model

import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style

internal data class HistoryUiModel(
    val id: Long,
    val dDayLabel: String,
    val dateLabel: String,
    val title: String,
    val category: Category,
    val purpose: Purpose,
    val style: Style,
    val audience: Audience,
)
