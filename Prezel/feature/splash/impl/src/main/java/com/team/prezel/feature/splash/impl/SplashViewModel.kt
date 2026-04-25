package com.team.prezel.feature.splash.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.result.auth.LoginStatusResult
import com.team.prezel.core.domain.usecase.auth.CheckLoginStatusUseCase
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
) : BaseViewModel<SplashUiState, SplashUiIntent, SplashUiEffect>(
        SplashUiState(),
    ) {
    override fun onIntent(intent: SplashUiIntent) {
        when (intent) {
            SplashUiIntent.CheckLoginStatus -> checkLoginStatus()
        }
    }

    private fun checkLoginStatus() {
        updateState { copy(isLoading = true) }

        viewModelScope
            .launch {
                when (val result = checkLoginStatusUseCase()) {
                    LoginStatusResult.Authenticated -> sendEffect(SplashUiEffect.NavigateToHome)
                    LoginStatusResult.Unauthenticated -> sendEffect(SplashUiEffect.NavigateToLogin)
                    is LoginStatusResult.RetryableFailure -> {
                        Timber.w(result.throwable, "로그인 상태 확인에 실패했습니다. 잠시 후 다시 시도해 주세요.")
                        sendEffect(SplashUiEffect.ShowRetryableFailureMessage)
                    }
                }
            }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }
}
