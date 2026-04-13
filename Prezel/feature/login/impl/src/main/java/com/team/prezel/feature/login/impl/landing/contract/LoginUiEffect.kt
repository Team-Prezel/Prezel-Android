package com.team.prezel.feature.login.impl.landing.contract

import com.team.prezel.core.auth.model.AuthProvider
import com.team.prezel.core.ui.UiEffect
import com.team.prezel.feature.login.impl.landing.model.LoginUiMessage

internal sealed interface LoginUiEffect : UiEffect {
    data class LaunchLogin(
        val provider: AuthProvider,
    ) : LoginUiEffect

    data object NavigateToTerms : LoginUiEffect

    data class ShowMessage(
        val message: LoginUiMessage,
    ) : LoginUiEffect
}
