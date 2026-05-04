package com.team.prezel.feature.login.impl.landing.contract

import com.team.prezel.core.auth.model.AuthResult
import com.team.prezel.core.ui.base.UiIntent

internal sealed interface LoginUiIntent : UiIntent {
    data object OnClickLogin : LoginUiIntent

    data class OnLoginResult(
        val result: AuthResult,
    ) : LoginUiIntent
}
