package com.team.prezel.feature.login.impl.landing

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.auth.model.AuthProvider
import com.team.prezel.core.auth.model.AuthResult
import com.team.prezel.core.domain.usecase.auth.LoginUseCase
import com.team.prezel.core.ui.BaseViewModel
import com.team.prezel.feature.login.impl.landing.contract.LoginUiEffect
import com.team.prezel.feature.login.impl.landing.contract.LoginUiIntent
import com.team.prezel.feature.login.impl.landing.contract.LoginUiState
import com.team.prezel.feature.login.impl.landing.model.LoginUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : BaseViewModel<LoginUiState, LoginUiIntent, LoginUiEffect>(LoginUiState()) {
    override fun onIntent(intent: LoginUiIntent) {
        when (intent) {
            is LoginUiIntent.OnClickLogin -> handleClickLogin(provider = intent.provider)
            is LoginUiIntent.OnLoginResult -> handleLoginResult(result = intent.result)
        }
    }

    private fun handleClickLogin(provider: AuthProvider) {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState {
                copy(
                    isLoading = true,
                    pendingProvider = provider,
                )
            }
            sendEffect(LoginUiEffect.LaunchLogin(provider = provider))
        }
    }

    private fun handleLoginResult(result: AuthResult) {
        viewModelScope.launch {
            when (result) {
                is AuthResult.Success -> handleServerLogin(idToken = result.idToken)
                AuthResult.Cancelled -> {
                    updateState { copy(isLoading = false, pendingProvider = null) }
                    sendEffect(LoginUiEffect.ShowMessage(LoginUiMessage.LOGIN_CANCELLED))
                }

                is AuthResult.Failure -> {
                    updateState { copy(isLoading = false, pendingProvider = null) }
                    sendEffect(LoginUiEffect.ShowMessage(result.toUiMessage()))
                }
            }
        }
    }

    private suspend fun handleServerLogin(idToken: String) {
        loginUseCase(idToken = idToken).fold(
            onSuccess = {
                updateState { copy(isLoading = false, pendingProvider = null) }
                sendEffect(LoginUiEffect.NavigateToTerms)
            },
            onFailure = {
                updateState { copy(isLoading = false, pendingProvider = null) }
                sendEffect(LoginUiEffect.ShowMessage(LoginUiMessage.LOGIN_FAILED_UNKNOWN))
            },
        )
    }

    private fun AuthResult.Failure.toUiMessage(): LoginUiMessage =
        when (this) {
            AuthResult.Failure.RateLimited -> LoginUiMessage.LOGIN_FAILED_RATE_LIMITED
            AuthResult.Failure.Unknown -> LoginUiMessage.LOGIN_FAILED_UNKNOWN
        }
}
