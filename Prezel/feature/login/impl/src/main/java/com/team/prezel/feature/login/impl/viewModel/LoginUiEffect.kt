package com.team.prezel.feature.login.impl.viewModel

sealed interface LoginUiEffect {
    data object NavigateToHome : LoginUiEffect
}
