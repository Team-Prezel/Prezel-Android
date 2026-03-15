package com.team.prezel.feature.splash.impl.viewModel

sealed interface SplashUiEffect {
    data object NavigateToHome : SplashUiEffect

    data object NavigateToLogin : SplashUiEffect
}
