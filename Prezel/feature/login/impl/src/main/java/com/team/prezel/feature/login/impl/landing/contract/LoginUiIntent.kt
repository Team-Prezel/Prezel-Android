package com.team.prezel.feature.login.impl.landing.contract

import com.team.prezel.core.auth.model.AuthProvider
import com.team.prezel.core.auth.model.AuthResult

internal sealed interface LoginUiIntent {
    data class OnClickLogin(
        val provider: AuthProvider,
    ) : LoginUiIntent

    data class OnLoginResult(
        val result: AuthResult,
    ) : LoginUiIntent
}
