package com.team.prezel.feature.login.impl.landing.contract

import com.team.prezel.core.ui.base.UiEffect

internal sealed interface LoginUiEffect : UiEffect {
    data object NavigateToHome : LoginUiEffect
}
