package com.team.prezel.feature.profile.impl

sealed interface ProfileUiEffect {
    data object NavigateToLogin : ProfileUiEffect

    data class ShowSnackbar(
        val message: String,
    ) : ProfileUiEffect
}
