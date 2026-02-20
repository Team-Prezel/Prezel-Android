package com.team.prezel.feature.home.impl

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data object LoadFailed : HomeUiState
}
