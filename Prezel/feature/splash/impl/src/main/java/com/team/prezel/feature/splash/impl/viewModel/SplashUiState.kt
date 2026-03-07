package com.team.prezel.feature.splash.impl.viewModel

import androidx.compose.runtime.Immutable

@Immutable
sealed interface SplashUiState {
    data object Loading : SplashUiState
}
