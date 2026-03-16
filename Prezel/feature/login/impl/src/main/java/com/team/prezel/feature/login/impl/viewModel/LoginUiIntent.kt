package com.team.prezel.feature.login.impl.viewModel

sealed interface LoginUiIntent {
    data object OnClickLogin : LoginUiIntent

    data object LoginSucceeded : LoginUiIntent

    data class LoginFailed(
        val message: String? = null,
    ) : LoginUiIntent
}
