package com.team.prezel.feature.my.impl.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.my.impl.model.MyUiMessage

sealed interface MyUiEffect : UiEffect {
    data object NavigateToLogin : MyUiEffect

    data class ShowMessage(
        val message: MyUiMessage,
    ) : MyUiEffect
}
