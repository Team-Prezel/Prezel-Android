package com.team.prezel.feature.splash.impl.contract

import com.team.prezel.core.ui.base.UiIntent

sealed interface SplashUiIntent : UiIntent {
    data object CheckLoginStatus : SplashUiIntent
}
