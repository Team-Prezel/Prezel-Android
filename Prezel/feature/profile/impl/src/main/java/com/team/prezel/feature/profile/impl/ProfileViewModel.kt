package com.team.prezel.feature.profile.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.prezel.core.auth.AuthManager
import com.team.prezel.core.domain.usecase.auth.LogoutUseCase
import com.team.prezel.core.domain.usecase.auth.WithdrawUseCase
import com.team.prezel.core.model.auth.WithdrawReason
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
                _uiState.update { it.copy(isLoading = true) }
                val result =
                    logoutUseCase().fold(
                        onSuccess = { authManager.logout() },
                        onFailure = { Result.failure(it) },
                    )
                _uiState.update { it.copy(isLoading = false) }
                if (result.isSuccess) {
                    Timber.tag("ProfileTest").d("로그아웃에 성공했습니다.")
                    _uiEffect.emit(ProfileUiEffect.NavigateToLogin)
                } else {
                    Timber.tag("ProfileTest").e(result.exceptionOrNull(), "로그아웃에 실패했습니다.")
                    _uiEffect.emit(ProfileUiEffect.ShowSnackbar("로그아웃에 실패했습니다."))
                }
            }
        }

        fun withdraw() {
            if (_uiState.value.isLoading) return

            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                val result =
                    withdrawUseCase(
                        reason = WithdrawReason.Other("임시 테스트 탈퇴"),
                    ).fold(
                        onSuccess = { authManager.logout() },
                        onFailure = { Result.failure(it) },
                    )
                _uiState.update { it.copy(isLoading = false) }
                if (result.isSuccess) {
                    Timber.tag("ProfileTest").d("회원탈퇴에 성공했습니다.")
                    _uiEffect.emit(ProfileUiEffect.NavigateToLogin)
                } else {
                    Timber.tag("ProfileTest").e(result.exceptionOrNull(), "회원탈퇴에 실패했습니다.")
                    _uiEffect.emit(ProfileUiEffect.ShowSnackbar("회원탈퇴에 실패했습니다."))
                }
            }
        }
    }
