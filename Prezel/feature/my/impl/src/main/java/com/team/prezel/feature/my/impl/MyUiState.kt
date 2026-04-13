package com.team.prezel.feature.my.impl

sealed interface MyUiState {
    data object Loading : MyUiState

    data object LoadFailed : MyUiState
}
