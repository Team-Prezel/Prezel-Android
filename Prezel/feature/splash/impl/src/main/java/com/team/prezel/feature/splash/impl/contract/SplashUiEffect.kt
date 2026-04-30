package com.team.prezel.feature.splash.impl.contract

import com.team.prezel.core.ui.base.UiEffect

sealed interface SplashUiEffect : UiEffect {
    data object NavigateToHome : SplashUiEffect

    data object NavigateToLogin : SplashUiEffect

    data object ShowRetryableFailureMessage : SplashUiEffect
}
