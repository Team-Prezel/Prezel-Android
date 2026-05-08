package com.team.prezel.feature.home.impl.main.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.home.impl.main.model.HomeUiMessage

internal sealed interface HomeUiEffect : UiEffect {
    data class ShowMessage(
        val message: HomeUiMessage,
    ) : HomeUiEffect
}
