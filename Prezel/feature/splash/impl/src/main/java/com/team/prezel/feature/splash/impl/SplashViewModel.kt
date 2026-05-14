package com.team.prezel.feature.splash.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.auth.CheckLoginStatusUseCase
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.splash.impl.contract.SplashUiEffect
import com.team.prezel.feature.splash.impl.contract.SplashUiIntent
import com.team.prezel.feature.splash.impl.contract.SplashUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
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
        viewModelScope.launch { sendEffect(SplashUiEffect.NavigateToHome) }
        return

        updateState { copy(isLoading = true) }

        viewModelScope
            .launch {
                checkLoginStatusUseCase()
                    .onSuccess { user ->
                        user?.let { routeUser(user) } ?: sendEffect(SplashUiEffect.NavigateToLogin)
                    }.onFailure { exception ->
                        Timber.e(exception)
                        sendEffect(SplashUiEffect.ShowRetryableFailureMessage)
                        sendEffect(SplashUiEffect.NavigateToLogin)
                    }
            }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private suspend fun routeUser(user: User) {
        when {
            !user.isTermsAgreement -> sendEffect(SplashUiEffect.NavigateToTerms)
            !user.isProfileComplete -> sendEffect(SplashUiEffect.NavigateToCreateProfile)
            else -> sendEffect(SplashUiEffect.NavigateToHome)
        }
    }
}
