package com.team.prezel.feature.login.impl.viewModel

sealed interface LoginUiIntent {
    data object OnClickLogin : LoginUiIntent

    data object OnLoginSuccess : LoginUiIntent

    data class OnLoginFailure(
        val message: String,
    ) : LoginUiIntent
}
