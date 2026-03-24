package com.team.prezel.feature.login.impl.viewModel

sealed interface LoginUiIntent {
    data object OnClickLogin : LoginUiIntent
}
