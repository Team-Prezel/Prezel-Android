package com.team.prezel.feature.login.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.auth.model.AuthResult
import com.team.prezel.core.domain.usecase.auth.CheckLoginStatusUseCase
import com.team.prezel.core.domain.usecase.auth.LoginUseCase
import com.team.prezel.core.model.auth.AuthCheckResult
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.login.impl.contract.LoginUiEffect
import com.team.prezel.feature.login.impl.contract.LoginUiIntent
import com.team.prezel.feature.login.impl.contract.LoginUiState
import com.team.prezel.feature.login.impl.model.LoginUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase,
) : BaseViewModel<LoginUiState, LoginUiIntent, LoginUiEffect>(LoginUiState()) {
    override fun onIntent(intent: LoginUiIntent) {
        when (intent) {
            LoginUiIntent.OnClickLogin -> handleClickLogin()
            is LoginUiIntent.OnLoginResult -> handleLoginResult(result = intent.result)
        }
    }

    private fun handleClickLogin() {
        if (currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            sendEffect(LoginUiEffect.LaunchLogin)
        }
    }

    private fun handleLoginResult(result: AuthResult) {
        viewModelScope
            .launch {
                when (result) {
                    is AuthResult.Success -> handleServerLogin(idToken = result.idToken)
                    is AuthResult.Failure -> sendEffect(LoginUiEffect.ShowMessage(LoginUiMessage.LOGIN_FAILED_UNKNOWN))
                    AuthResult.Cancelled -> sendEffect(LoginUiEffect.ShowMessage(LoginUiMessage.LOGIN_CANCELLED))
                }
            }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private suspend fun handleServerLogin(idToken: String) {
        loginUseCase(idToken = idToken)
            .onSuccess {
                when (checkLoginStatusUseCase().first { result -> result != AuthCheckResult.Loading }) {
                    AuthCheckResult.Authenticated -> sendEffect(LoginUiEffect.NavigateToHome)
                    AuthCheckResult.NeedsTermsAgreement -> sendEffect(LoginUiEffect.NavigateToTerms)
                    AuthCheckResult.NeedsProfileCompletion -> sendEffect(LoginUiEffect.NavigateToCreateProfile)
                    AuthCheckResult.Unauthenticated,
                    AuthCheckResult.RetryableFailure,
                    -> sendEffect(LoginUiEffect.ShowMessage(LoginUiMessage.LOGIN_FAILED_UNKNOWN))

                    AuthCheckResult.Loading -> Unit
                }
            }.onFailure { sendEffect(LoginUiEffect.ShowMessage(LoginUiMessage.LOGIN_FAILED_UNKNOWN)) }
    }
}
