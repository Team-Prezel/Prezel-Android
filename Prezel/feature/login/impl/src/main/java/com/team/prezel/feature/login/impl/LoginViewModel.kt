package com.team.prezel.feature.login.impl.landing

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.auth.model.AuthResult
import com.team.prezel.core.domain.usecase.auth.LoginUseCase
import com.team.prezel.core.domain.usecase.auth.ResolveAuthStepUseCase
import com.team.prezel.core.model.auth.AuthStep
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.login.impl.landing.contract.LoginUiEffect
import com.team.prezel.feature.login.impl.landing.contract.LoginUiIntent
import com.team.prezel.feature.login.impl.landing.contract.LoginUiState
import com.team.prezel.feature.login.impl.landing.model.LoginUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val resolveAuthStepUseCase: ResolveAuthStepUseCase,
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
                when (resolveAuthStepUseCase().first { step -> step != AuthStep.Loading }) {
                    AuthStep.Ready -> sendEffect(LoginUiEffect.NavigateToHome)
                    AuthStep.TermsRequired -> sendEffect(LoginUiEffect.NavigateToTerms)
                    AuthStep.ProfileRequired -> sendEffect(LoginUiEffect.NavigateToCreateProfile)
                    AuthStep.Unauthenticated,
                    AuthStep.RetryableFailure,
                    AuthStep.Loading,
                    -> sendEffect(LoginUiEffect.ShowMessage(LoginUiMessage.LOGIN_FAILED_UNKNOWN))
                }
            }
            .onFailure { sendEffect(LoginUiEffect.ShowMessage(LoginUiMessage.LOGIN_FAILED_UNKNOWN)) }
    }
}
