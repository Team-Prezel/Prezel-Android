package com.team.prezel.feature.splash.impl.viewModel

import com.team.prezel.core.ui.UiEffect

sealed interface SplashUiEffect : UiEffect {
    data object NavigateToHome : SplashUiEffect

    data object NavigateToLogin : SplashUiEffect
}
