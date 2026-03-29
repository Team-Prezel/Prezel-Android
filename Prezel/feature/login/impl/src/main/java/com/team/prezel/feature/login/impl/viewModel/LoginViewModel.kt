package com.team.prezel.feature.login.impl.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(LoginUiState())
        val uiState: StateFlow<LoginUiState> = _uiState

        private val _uiEffect = Channel<LoginUiEffect>()
        val uiEffect: Flow<LoginUiEffect> = _uiEffect.receiveAsFlow()

        fun onIntent(intent: LoginUiIntent) {
            when (intent) {
                LoginUiIntent.OnClickLogin -> handleClickLogin()
                LoginUiIntent.OnLoginSuccess -> handleLoginSuccess()
                is LoginUiIntent.OnLoginFailure -> handleLoginFailure(intent.message)
            }
        }

        private fun reduce(reducer: (LoginUiState) -> LoginUiState) {
            _uiState.update(reducer)
        }

        private fun handleClickLogin() {
            if (_uiState.value.isLoading) return

            viewModelScope.launch {
                reduce { it.copy(isLoading = true) }
                _uiEffect.send(LoginUiEffect.LaunchKakaoLogin)
            }
        }

        private fun handleLoginSuccess() {
            viewModelScope.launch {
                reduce { it.copy(isLoading = false) }
                _uiEffect.send(LoginUiEffect.NavigateToHome)
            }
        }

        private fun handleLoginFailure(message: String) {
            viewModelScope.launch {
                reduce { it.copy(isLoading = false) }
                _uiEffect.send(LoginUiEffect.ShowSnackbar(message))
            }
        }
    }
