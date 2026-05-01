package com.team.prezel.feature.login.impl.landing.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.login.impl.landing.model.LoginUiMessage

internal sealed interface LoginUiEffect : UiEffect {
    data object LaunchLogin : LoginUiEffect

    data object NavigateToHome : LoginUiEffect

    data object NavigateToTerms : LoginUiEffect

    data class ShowMessage(
        val message: LoginUiMessage,
    ) : LoginUiEffect
}
