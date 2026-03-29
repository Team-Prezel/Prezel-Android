package com.team.prezel.feature.login.impl.viewModel

import com.team.prezel.core.auth.model.AuthProvider
import com.team.prezel.core.auth.model.AuthResult

sealed interface LoginUiIntent {
    data class OnClickLogin(
        val provider: AuthProvider,
    ) : LoginUiIntent

    data class OnLoginResult(
        val result: AuthResult,
    ) : LoginUiIntent
}
