package com.team.prezel.feature.profile.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.prezel.core.auth.AuthManager
import com.team.prezel.core.domain.result.auth.AuthActionResult
import com.team.prezel.core.domain.usecase.auth.LogoutUseCase
import com.team.prezel.core.domain.usecase.auth.WithdrawUseCase
import com.team.prezel.core.model.auth.WithdrawReason
import com.team.prezel.feature.profile.impl.contract.ProfileUiEffect
import com.team.prezel.feature.profile.impl.contract.ProfileUiState
import com.team.prezel.feature.profile.impl.model.ProfileUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val authManager: AuthManager,
        private val logoutUseCase: LogoutUseCase,
        private val withdrawUseCase: WithdrawUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ProfileUiState())
        val uiState = _uiState.asStateFlow()

        private val _uiEffect = MutableSharedFlow<ProfileUiEffect>()
        val uiEffect = _uiEffect.asSharedFlow()

        fun logout() {
            if (_uiState.value.isLoading) return

            viewModelScope.launch {
                try {
                    _uiState.update { it.copy(isLoading = true) }
                    val result = logoutUseCase()
                    handleAuthActionResult(
                        result = result,
                        failureLog = "로그아웃에 실패했습니다.",
                        failureMessage = ProfileUiMessage.LOGOUT_FAILED,
                    )
                } finally {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }

        fun withdraw() {
            if (_uiState.value.isLoading) return

            viewModelScope.launch {
                try {
                    _uiState.update { it.copy(isLoading = true) }
                    val result =
                        withdrawUseCase(
                            reason = WithdrawReason.Other("임시 테스트 탈퇴"),
                        )
                    handleAuthActionResult(
                        result = result,
                        failureLog = "회원탈퇴에 실패했습니다.",
                        failureMessage = ProfileUiMessage.WITHDRAW_FAILED,
                    )
                } finally {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }

        private suspend fun handleAuthActionResult(
            result: AuthActionResult,
            failureLog: String,
            failureMessage: ProfileUiMessage,
        ) {
            when (result) {
                AuthActionResult.Success -> {
                    authManager
                        .logout()
                        .onFailure { throwable ->
                            Timber.tag("ProfileTest").w(throwable, "로컬 인증 세션 정리에 실패했습니다.")
                        }
                    _uiEffect.emit(ProfileUiEffect.NavigateToLogin)
                }

                AuthActionResult.AuthenticationRequired -> {
                    authManager.clearCurrentProvider()
                    _uiEffect.emit(ProfileUiEffect.ShowMessage(ProfileUiMessage.AUTHENTICATION_EXPIRED))
                    _uiEffect.emit(ProfileUiEffect.NavigateToLogin)
                }

                is AuthActionResult.Failure -> {
                    Timber.tag("ProfileTest").e(result.throwable, failureLog)
                    _uiEffect.emit(ProfileUiEffect.ShowMessage(failureMessage))
                }
            }
        }
    }
