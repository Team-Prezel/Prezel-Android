package com.team.prezel.feature.splash.impl.contract

import com.team.prezel.core.ui.UiIntent

sealed interface SplashUiIntent : UiIntent {
    data object CheckLoginStatus : SplashUiIntent
}
