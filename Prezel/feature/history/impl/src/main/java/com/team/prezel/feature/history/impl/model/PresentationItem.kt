package com.team.prezel.feature.history.impl.model

import kotlinx.collections.immutable.ImmutableList

internal data class PresentationItem(
    val id: Long,
    val dDayLabel: String,
    val dateLabel: String,
    val title: String,
    val chips: ImmutableList<HistoryChipUiModel>,
)

internal data class HistoryChipUiModel(
    val label: String,
    val highlighted: Boolean = false,
)
