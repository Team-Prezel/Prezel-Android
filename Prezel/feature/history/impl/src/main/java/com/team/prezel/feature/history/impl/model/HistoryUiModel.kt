package com.team.prezel.feature.history.impl.model

import com.team.prezel.core.model.presentation.Category
import kotlinx.collections.immutable.ImmutableList

internal data class HistoryUiModel(
    val id: Long,
    val category: Category,
    val dDayLabel: String,
    val dateLabel: String,
    val title: String,
    val chips: ImmutableList<HistoryChipUiModel>,
)

internal data class HistoryChipUiModel(
    val label: String,
    val highlighted: Boolean = false,
)
