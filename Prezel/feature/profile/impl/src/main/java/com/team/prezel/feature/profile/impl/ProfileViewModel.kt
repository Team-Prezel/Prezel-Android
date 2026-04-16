package com.team.prezel.feature.profile.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.profile.ValidateNicknameUseCase
import com.team.prezel.core.model.profile.Nickname
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.ui.BaseViewModel
import com.team.prezel.feature.profile.impl.contract.NicknameValidationState
import com.team.prezel.feature.profile.impl.contract.ProfileUiEffect
import com.team.prezel.feature.profile.impl.contract.ProfileUiIntent
import com.team.prezel.feature.profile.impl.contract.ProfileUiState
import com.team.prezel.feature.profile.impl.model.ProfileUiMessage
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel(assistedFactory = ProfileViewModel.Factory::class)
internal class ProfileViewModel @AssistedInject constructor(
    @Assisted initialState: ProfileUiState,
    private val validateNicknameUseCase: ValidateNicknameUseCase,
) : BaseViewModel<ProfileUiState, ProfileUiIntent, ProfileUiEffect>(initialState) {
    private val nicknameInput = MutableStateFlow(currentState.nickname)

    @AssistedFactory
    interface Factory {
        fun create(initialState: ProfileUiState): ProfileViewModel
    }

    init {
        viewModelScope.launch {
            nicknameInput
                .debounce(NICKNAME_VALIDATION_DEBOUNCE_MILLIS)
                .distinctUntilChanged()
                .collectLatest(::validateNickname)
        }
    }

    override fun onIntent(intent: ProfileUiIntent) {
        when (intent) {
            is ProfileUiIntent.OnNicknameChanged -> handleNicknameChanged(intent.nickname)
            is ProfileUiIntent.OnProfileImageChanged -> handleProfileImageChanged(intent.profileUrl)

            ProfileUiIntent.OnClickSubmit -> submitProfile()
        }
    }

    private fun handleNicknameChanged(nickname: String) {
        val sanitizedNickname = nickname.take(Nickname.MAX_LENGTH)
        if (sanitizedNickname == currentState.nickname) return

        updateState {
            val validationState = if (sanitizedNickname.isBlank()) NicknameValidationState.Unchecked else NicknameValidationState.Checking

            updateProfile(
                nickname = sanitizedNickname,
                nicknameValidation = validationState,
            )
        }

        nicknameInput.value = sanitizedNickname
    }

    private fun handleProfileImageChanged(profileUrl: String) {
        if (profileUrl == currentState.profileImage.url) return

        updateState {
            updateProfile(
                profileImage = User.ProfileImage(
                    url = profileUrl,
                    isDefault = profileUrl.isBlank(),
                ),
            )
        }
    }

    private suspend fun validateNickname(nickname: String) {
        if (nickname.isBlank()) {
            updateState { updateProfile(nicknameValidation = NicknameValidationState.Unchecked) }
            return
        }

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

        updateState { updateProfile(nicknameValidation = validationState) }
    }

    private fun Nickname.InvalidReason.toValidationState(): NicknameValidationState =
        when (this) {
            Nickname.InvalidReason.TOO_SHORT -> NicknameValidationState.TooShort
            Nickname.InvalidReason.TOO_LONG -> NicknameValidationState.TooLong
            Nickname.InvalidReason.INVALID_CHARACTER -> NicknameValidationState.InvalidCharacter
        }

    private fun submitProfile() {
        if (currentState.nicknameValidation != NicknameValidationState.Available) return

        viewModelScope.launch {
            // todo: 닉네임 생성 API 호출
            sendEffect(ProfileUiEffect.NavigateToHome)
        }
    }

    private companion object {
        const val NICKNAME_VALIDATION_DEBOUNCE_MILLIS = 300L
    }
}
