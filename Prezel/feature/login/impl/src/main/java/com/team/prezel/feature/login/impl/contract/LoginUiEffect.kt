package com.team.prezel.feature.login.impl.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.login.impl.model.LoginUiMessage

internal sealed interface LoginUiEffect : UiEffect {
    data object LaunchLogin : LoginUiEffect

    data object NavigateToHome : LoginUiEffect

    data object NavigateToTerms : LoginUiEffect

    data object NavigateToCreateProfile : LoginUiEffect

    data class ShowMessage(
        val message: LoginUiMessage,
    ) : LoginUiEffect
}
