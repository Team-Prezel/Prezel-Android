package com.team.prezel.feature.login.impl.landing.contract

import com.team.prezel.core.auth.model.AuthProvider
import com.team.prezel.core.auth.model.AuthResult
import com.team.prezel.core.ui.UiIntent

internal sealed interface LoginUiIntent : UiIntent {
    data class OnClickLogin(
        val provider: AuthProvider,
    ) : LoginUiIntent

    data class OnLoginResult(
        val provider: AuthProvider,
        val result: AuthResult,
    ) : LoginUiIntent
}
