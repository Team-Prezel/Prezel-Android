package com.team.prezel.feature.login.impl.viewModel

sealed interface LoginUiEffect {
    data object LaunchKakaoLogin : LoginUiEffect

    data object NavigateToHome : LoginUiEffect

    data class ShowSnackbar(
        val message: String,
    ) : LoginUiEffect
}
