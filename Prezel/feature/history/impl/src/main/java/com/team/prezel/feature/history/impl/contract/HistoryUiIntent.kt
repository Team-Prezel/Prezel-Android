package com.team.prezel.feature.history.impl.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface HistoryUiIntent : UiIntent {
    data object FetchData : HistoryUiIntent

    data class ClickItem(
        val presentationId: Long,
    ) : HistoryUiIntent
}
