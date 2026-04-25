package com.team.prezel.feature.home.impl.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.home.impl.model.HomeUiMessage

internal sealed interface HomeUiEffect : UiEffect {
    data class ShowMessage(
        val message: HomeUiMessage,
    ) : HomeUiEffect
}
