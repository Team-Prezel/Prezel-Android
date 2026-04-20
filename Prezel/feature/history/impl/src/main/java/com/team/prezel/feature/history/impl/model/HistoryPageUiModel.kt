package com.team.prezel.feature.history.impl.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class HistoryPageUiModel(
    val type: HistoryPageType,
    val items: ImmutableList<HistoryUiModel>,
)

internal enum class HistoryPageType {
    PREPARING,
    COMPLETED,
}
