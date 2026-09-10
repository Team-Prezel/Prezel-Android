package com.team.prezel.feature.history.impl.contract

import com.team.prezel.core.ui.base.UiIntent
import com.team.prezel.feature.history.impl.model.HistoryPageType
import com.team.prezel.feature.history.impl.model.HistoryUiModel

internal sealed interface HistoryUiIntent : UiIntent {
    data object FetchData : HistoryUiIntent

    data class ClickItem(
        val item: HistoryUiModel,
        val pageType: HistoryPageType,
    ) : HistoryUiIntent
}
