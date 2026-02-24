package com.team.prezel.feature.history.impl

sealed interface HistoryUiState {
    data object Loading : HistoryUiState

    data object LoadFailed : HistoryUiState
}
