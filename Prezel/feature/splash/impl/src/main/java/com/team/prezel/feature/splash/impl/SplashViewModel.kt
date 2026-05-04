package com.team.prezel.feature.splash.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.auth.CheckLoginStatusUseCase
import com.team.prezel.core.model.auth.AuthCheckResult
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.splash.impl.contract.SplashUiEffect
import com.team.prezel.feature.splash.impl.contract.SplashUiIntent
import com.team.prezel.feature.splash.impl.contract.SplashUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase,
) : BaseViewModel<SplashUiState, SplashUiIntent, SplashUiEffect>(SplashUiState()) {
    override fun onIntent(intent: SplashUiIntent) {
        when (intent) {
            SplashUiIntent.CheckLoginStatus -> checkLoginStatus()
        }
    }

    private fun checkLoginStatus() {
        updateState { copy(isLoading = true) }

        viewModelScope
            .launch {
                when (checkLoginStatusUseCase().first { result -> result != AuthCheckResult.Loading }) {
                    AuthCheckResult.Authenticated -> sendEffect(SplashUiEffect.NavigateToHome)
                    AuthCheckResult.NeedsTermsAgreement -> sendEffect(SplashUiEffect.NavigateToTerms)
                    AuthCheckResult.NeedsProfileCompletion -> sendEffect(SplashUiEffect.NavigateToCreateProfile)
                    AuthCheckResult.Unauthenticated -> sendEffect(SplashUiEffect.NavigateToLogin)
                    AuthCheckResult.RetryableFailure -> sendEffect(SplashUiEffect.ShowRetryableFailureMessage)
                    AuthCheckResult.Loading -> Unit
                }
            }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }
}
