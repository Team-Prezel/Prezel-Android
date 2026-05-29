package com.team.prezel.feature.home.impl.main.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface HomeUiIntent : UiIntent {
    data object FetchData : HomeUiIntent

    data class ClickCardGraphItem(
        val presentationId: Long,
        val index: Int,
    ) : HomeUiIntent
}
