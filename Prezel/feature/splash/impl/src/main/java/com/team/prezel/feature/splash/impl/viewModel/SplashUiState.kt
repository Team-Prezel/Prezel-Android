package com.team.prezel.feature.splash.impl.viewModel

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.UiState

@Immutable
internal data class SplashUiState(
    val isLoading: Boolean = false,
) : UiState
