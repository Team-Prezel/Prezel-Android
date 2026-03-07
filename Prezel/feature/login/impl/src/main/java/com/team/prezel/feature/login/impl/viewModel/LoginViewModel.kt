package com.team.prezel.feature.login.impl.viewModel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@Stable
@HiltViewModel
class LoginViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(LoginUiState.Loading)
        val uiState: StateFlow<LoginUiState> = _uiState

        private val _uiEffect = Channel<LoginUiEffect>()
        val uiEffect: Flow<LoginUiEffect> = _uiEffect.receiveAsFlow()

        fun login() {
            _uiEffect.trySend(LoginUiEffect.NavigateToHome)
        }
    }
