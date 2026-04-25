package com.team.prezel.feature.splash.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.base.UiState

@Immutable
internal data class SplashUiState(
    val isLoading: Boolean = false,
) : UiState
