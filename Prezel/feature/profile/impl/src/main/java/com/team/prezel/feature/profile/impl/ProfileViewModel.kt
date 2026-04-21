package com.team.prezel.feature.profile.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.user.FetchUserInfoUseCase
import com.team.prezel.core.domain.usecase.user.ValidateNicknameUseCase
import com.team.prezel.core.model.profile.Nickname
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.ui.base.BaseViewModel
import com.team.prezel.feature.profile.impl.contract.ProfileUiEffect
import com.team.prezel.feature.profile.impl.contract.ProfileUiIntent
import com.team.prezel.feature.profile.impl.contract.ProfileUiState
import com.team.prezel.feature.profile.impl.contract.ProfileUiState.Content.Companion.toUiState
import com.team.prezel.feature.profile.impl.model.NicknameValidationState
import com.team.prezel.feature.profile.impl.model.ProfileUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    private val fetchUserInfoUseCase: FetchUserInfoUseCase,
    private val validateNicknameUseCase: ValidateNicknameUseCase,
) : BaseViewModel<ProfileUiState, ProfileUiIntent, ProfileUiEffect>(ProfileUiState.Loading) {
    private val nicknameChanges = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            nicknameChanges
                .filterNotNull()
                .debounce(NICKNAME_VALIDATION_DEBOUNCE_MILLIS)
                .distinctUntilChanged()
                .collectLatest(::validateNickname)
        }
    }

    override fun onIntent(intent: ProfileUiIntent) {
        when (intent) {
            ProfileUiIntent.FetchData -> fetchUserInfo()
            is ProfileUiIntent.UpdateNickname -> handleNicknameChanged(intent.nickname)
            is ProfileUiIntent.UpdateProfileImage -> handleProfileImageChanged(intent.profileUrl)
            ProfileUiIntent.SubmitProfile -> submitProfile()
        }
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            fetchUserInfoUseCase()
                .onSuccess { user -> updateState { user.toUiState() } }
                .onFailure { throwable ->
                    sendEffect(ProfileUiEffect.ShowMessage(ProfileUiMessage.FETCH_USER_INFO_FAILED))
                    Timber.e(throwable)
                }
        }
    }

    private fun handleNicknameChanged(nickname: String) {
        val uiState = currentState as? ProfileUiState.Content ?: return

        val sanitizedNickname = nickname
            .filterNot(Char::isWhitespace)
            .take(Nickname.MAX_LENGTH)
        if (sanitizedNickname == uiState.nickname) return

        updateState {
            val validationState = when {
                sanitizedNickname.isBlank() && uiState.nickname.isNotBlank() -> NicknameValidationState.TooShort
                sanitizedNickname.isBlank() -> NicknameValidationState.Unchecked
                else -> NicknameValidationState.Checking
            }

            uiState.copy(
                nickname = sanitizedNickname,
                nicknameValidation = validationState,
            )
        }

        nicknameChanges.value = sanitizedNickname
    }

    private fun handleProfileImageChanged(profileUrl: String) {
        val uiState = currentState as? ProfileUiState.Content ?: return
        if (profileUrl == uiState.profileImage.url) return

        updateState {
            uiState.copy(
                profileImage = User.ProfileImage(
                    url = profileUrl,
                    isDefault = profileUrl.isBlank(),
                ),
            )
        }
    }

    private suspend fun validateNickname(nickname: String) {
        if (nickname.isBlank()) return

        val validationState = when (val result = validateNicknameUseCase(nickname)) {
            is ValidateNicknameUseCase.Result.Available -> NicknameValidationState.Available
            is ValidateNicknameUseCase.Result.Invalid -> {
                when (result) {
                    is ValidateNicknameUseCase.Result.Invalid.Format -> result.reason.toValidationState()
                    is ValidateNicknameUseCase.Result.Invalid.Duplicated -> NicknameValidationState.Duplicated
                }
            }

            is ValidateNicknameUseCase.Result.Error -> {
                sendEffect(ProfileUiEffect.ShowMessage(ProfileUiMessage.CHECK_NICKNAME_FAILED))
                NicknameValidationState.Unchecked
            }
        }

        updateState {
            val uiState = currentState as? ProfileUiState.Content ?: return@updateState currentState
            if (uiState.nickname != nickname) return@updateState currentState
            uiState.copy(nicknameValidation = validationState)
        }
    }

    private fun Nickname.InvalidReason.toValidationState(): NicknameValidationState =
        when (this) {
            Nickname.InvalidReason.TOO_SHORT -> NicknameValidationState.TooShort
            Nickname.InvalidReason.TOO_LONG -> NicknameValidationState.TooLong
            Nickname.InvalidReason.INVALID_CHARACTER -> NicknameValidationState.InvalidCharacter
        }

    private fun submitProfile() {
        val uiState = currentState as? ProfileUiState.Content ?: return
        if (!uiState.submitButtonEnabled) return

        viewModelScope.launch {
            // todo: 프로필 수정 API 호출 필요
//            patchUserProfileUseCase(fetchedState.profileImage, fetchedState.nickname)
//                .onSuccess {
//                    when(fetchedState) {
//                        is ProfileUiState.Create -> ProfileUiEffect.NavigateToHome
//                        is ProfileUiState.Edit -> ProfileUiEffect.OnBack
//                    }.let(sendEffect)
//                }
//                .onFailure { throwable ->
//                    sendEffect(ProfileUiEffect.ShowMessage(ProfileUiMessage.PATCH_USER_PROFILE_FAILED))
//                    Timber.e(throwable)
//                }
            sendEffect(ProfileUiEffect.NavigateToHome)
        }
    }

    private companion object {
        const val NICKNAME_VALIDATION_DEBOUNCE_MILLIS = 300L
    }
}
