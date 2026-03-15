package com.team.prezel.feature.login.impl.viewModel

import androidx.compose.runtime.Immutable

@Immutable
sealed interface LoginUiState {
    data object Idle : LoginUiState

    data object Loading : LoginUiState
}
