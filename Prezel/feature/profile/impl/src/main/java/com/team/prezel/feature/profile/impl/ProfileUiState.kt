package com.team.prezel.feature.profile.impl

sealed interface ProfileUiState {
    data object Loading : ProfileUiState

    data object LoadFailed : ProfileUiState
}
