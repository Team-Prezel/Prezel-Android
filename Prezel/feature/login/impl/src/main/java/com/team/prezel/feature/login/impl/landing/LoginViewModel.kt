package com.team.prezel.feature.login.impl.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.prezel.core.auth.model.AuthProvider
import com.team.prezel.core.auth.model.AuthResult
import com.team.prezel.feature.login.impl.landing.contract.LoginUiEffect
import com.team.prezel.feature.login.impl.landing.contract.LoginUiIntent
import com.team.prezel.feature.login.impl.landing.contract.LoginUiState
import com.team.prezel.feature.login.impl.landing.model.LoginUiMessage
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
internal class LoginViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(LoginUiState())
        val uiState: StateFlow<LoginUiState> = _uiState
        private val currentState: LoginUiState
            get() = uiState.value

        private val _uiEffect = Channel<LoginUiEffect>()
        val uiEffect: Flow<LoginUiEffect> = _uiEffect.receiveAsFlow()

        fun onIntent(intent: LoginUiIntent) {
            when (intent) {
                is LoginUiIntent.OnClickLogin -> handleClickLogin(provider = intent.provider)
                is LoginUiIntent.OnLoginResult -> handleLoginResult(result = intent.result)
            }
        }

        private fun update(reducer: LoginUiState.() -> LoginUiState) {
            _uiState.update(reducer)
        }

        private fun handleClickLogin(provider: AuthProvider) {
            if (currentState.isLoading) return

            viewModelScope.launch {
                update { copy(isLoading = true) }
//                _uiEffect.send(LoginUiEffect.LaunchLogin(provider = provider))
                _uiEffect.send(LoginUiEffect.NavigateToTerms)
            }
        }

        private fun handleLoginResult(result: AuthResult) {
            viewModelScope.launch {
                update { copy(isLoading = false) }

                when (result) {
                    AuthResult.Success -> _uiEffect.send(LoginUiEffect.NavigateToTerms)
                    AuthResult.Cancelled -> _uiEffect.send(LoginUiEffect.ShowMessage(LoginUiMessage.LoginCancelled))
                    is AuthResult.Failure -> _uiEffect.send(LoginUiEffect.ShowMessage(result.toUiMessage()))
                }
            }
        }

        private fun AuthResult.Failure.toUiMessage(): LoginUiMessage =
            when (this) {
                AuthResult.Failure.RateLimited -> LoginUiMessage.LoginFailedRateLimited
                AuthResult.Failure.Unknown -> LoginUiMessage.LoginFailedUnknown
            }
    }
