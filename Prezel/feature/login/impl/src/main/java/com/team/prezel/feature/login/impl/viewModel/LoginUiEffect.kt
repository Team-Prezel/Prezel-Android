package com.team.prezel.feature.login.impl.viewModel

import com.team.prezel.core.auth.model.AuthProvider
import com.team.prezel.feature.login.impl.model.LoginUiMessage

sealed interface LoginUiEffect {
    data class LaunchLogin(
        val provider: AuthProvider,
    ) : LoginUiEffect

    data object NavigateToHome : LoginUiEffect

    data class ShowMessage(
        val message: LoginUiMessage,
    ) : LoginUiEffect
}
