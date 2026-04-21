package com.team.prezel.feature.login.impl.landing

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.auth.model.AuthProvider
import com.team.prezel.core.auth.model.AuthResult
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.login.impl.BuildConfig
import com.team.prezel.feature.login.impl.landing.contract.LoginUiEffect
import com.team.prezel.feature.login.impl.landing.contract.LoginUiIntent
import com.team.prezel.feature.login.impl.landing.contract.LoginUiState
import com.team.prezel.feature.login.impl.landing.model.LoginUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor() : BaseViewModel<LoginUiState, LoginUiIntent, LoginUiEffect>(LoginUiState()) {
    override fun onIntent(intent: LoginUiIntent) {
        when (intent) {
            is LoginUiIntent.OnClickLogin -> handleClickLogin(provider = intent.provider)
            is LoginUiIntent.OnLoginResult -> handleLoginResult(result = intent.result)
        }
    }

    private fun handleClickLogin(provider: AuthProvider) {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            // todo: MVP 개발 완료 후 해당 조건 제거
            if (BuildConfig.DEBUG) {
                updateState { copy(isLoading = false) }
                sendEffect(LoginUiEffect.NavigateToTerms)
            } else {
                sendEffect(LoginUiEffect.LaunchLogin(provider = provider))
            }
        }
    }

    private fun handleLoginResult(result: AuthResult) {
        viewModelScope.launch {
            when (result) {
                AuthResult.Success -> fetchMyInfo()
                AuthResult.Cancelled -> {
                    sendEffect(LoginUiEffect.ShowMessage(LoginUiMessage.LoginCancelled))
                    updateState { copy(isLoading = false) }
                }

                is AuthResult.Failure -> {
                    sendEffect(LoginUiEffect.ShowMessage(result.toUiMessage()))
                    updateState { copy(isLoading = false) }
                }
            }
        }
    }

    private fun fetchMyInfo() {
        viewModelScope
            .launch {
                val isProfileCreateComplete = true
                if (isProfileCreateComplete) sendEffect(LoginUiEffect.NavigateToHome) else sendEffect(LoginUiEffect.NavigateToTerms)
            }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun AuthResult.Failure.toUiMessage(): LoginUiMessage =
        when (this) {
            AuthResult.Failure.RateLimited -> LoginUiMessage.LoginFailedRateLimited
            AuthResult.Failure.Unknown -> LoginUiMessage.LoginFailedUnknown
        }
}
