package com.team.prezel.feature.login.impl.landing.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.UiState

@Immutable
internal data class LoginUiState(
    val isLoading: Boolean = false,
) : UiState
