package com.team.prezel.feature.login.impl.landing.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.auth.model.AuthProvider
import com.team.prezel.core.ui.base.UiState

@Immutable
internal data class LoginUiState(
    val isLoading: Boolean = false,
    val pendingProvider: AuthProvider? = null,
) : UiState
