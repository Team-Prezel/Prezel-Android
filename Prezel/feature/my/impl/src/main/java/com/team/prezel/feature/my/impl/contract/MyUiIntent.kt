package com.team.prezel.feature.my.impl.contract

import com.team.prezel.core.ui.UiIntent

internal sealed interface MyUiIntent : UiIntent {
    data object FetchData : MyUiIntent
}
