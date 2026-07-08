package com.team.prezel.feature.history.impl.contract

import com.team.prezel.core.ui.base.UiIntent
import com.team.prezel.feature.history.impl.model.HistoryPageType

internal sealed interface HistoryUiIntent : UiIntent {
    data object FetchData : HistoryUiIntent

    data class ClickItem(
        val presentationId: Long,
        val pageType: HistoryPageType,
    ) : HistoryUiIntent
}
