package com.team.prezel.feature.history.impl.contract

import com.team.prezel.core.ui.UiIntent

internal sealed interface HistoryUiIntent : UiIntent {
    data object FetchData : HistoryUiIntent
}
