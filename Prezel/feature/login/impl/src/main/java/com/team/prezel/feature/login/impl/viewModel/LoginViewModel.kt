package com.team.prezel.feature.login.impl.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.prezel.core.data.auth.KakaoLoginManager
import com.team.prezel.core.data.auth.KakaoLoginResult
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
    constructor(
        private val kakaoLoginManager: KakaoLoginManager,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(LoginUiState())
        val uiState: StateFlow<LoginUiState> = _uiState

        private val _uiEffect = Channel<LoginUiEffect>()
        val uiEffect: Flow<LoginUiEffect> = _uiEffect.receiveAsFlow()

        fun onIntent(intent: LoginUiIntent) {
            when (intent) {
                is LoginUiIntent.OnClickLogin -> handleClickLogin(intent)
            }
        }

        private fun handleClickLogin(intent: LoginUiIntent.OnClickLogin) {
            if (_uiState.value.isLoginInProgress) return

            viewModelScope.launch {
                _uiState.update { it.copy(isLoginInProgress = true) }

                when (kakaoLoginManager.login(intent.context)) {
                    is KakaoLoginResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoginInProgress = false,
                                isNavigatingToHome = true,
                            )
                        }
                        _uiEffect.send(LoginUiEffect.NavigateToHome)
                    }

                    is KakaoLoginResult.RateLimited -> {
                        _uiState.update { it.copy(isLoginInProgress = false) }
                        _uiEffect.send(LoginUiEffect.ShowSnackbar(intent.rateLimitMessage))
                    }

                    is KakaoLoginResult.Failure -> {
                        _uiState.update { it.copy(isLoginInProgress = false) }
                        _uiEffect.send(LoginUiEffect.ShowSnackbar(intent.failureMessage))
                    }
                }
            }
        }
    }
