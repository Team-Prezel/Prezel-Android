package com.team.prezel.feature.login.impl.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface LoginUiIntent : UiIntent {
    data object OnClickLogin : LoginUiIntent
}
