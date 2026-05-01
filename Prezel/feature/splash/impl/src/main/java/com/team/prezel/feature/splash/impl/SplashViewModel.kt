package com.team.prezel.feature.splash.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.auth.CheckLoginStatusUseCase
import com.team.prezel.core.model.auth.LoginStatus
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
                when (checkLoginStatusUseCase().first { status -> status != LoginStatus.LOADING }) {
                    LoginStatus.AUTHENTICATED -> sendEffect(SplashUiEffect.NavigateToHome)
                    LoginStatus.UNAUTHENTICATED -> sendEffect(SplashUiEffect.NavigateToLogin)
                    LoginStatus.LOADING -> Unit
                }
            }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }
}
