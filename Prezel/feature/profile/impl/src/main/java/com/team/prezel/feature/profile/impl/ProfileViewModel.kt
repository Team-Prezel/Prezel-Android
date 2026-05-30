package com.team.prezel.feature.profile.impl

import androidx.lifecycle.viewModelScope
import com.team.prezel.core.domain.usecase.user.FetchUserInfoUseCase
import com.team.prezel.core.domain.usecase.user.PatchUserProfileUseCase
import com.team.prezel.core.domain.usecase.user.ValidateNicknameUseCase
import com.team.prezel.core.model.profile.Nickname
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
import java.io.File
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
internal class ProfileViewModel @Inject constructor(
    private val fetchUserInfoUseCase: FetchUserInfoUseCase,
    private val patchUserProfileUseCase: PatchUserProfileUseCase,
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

        fetchUserInfo()
    }

    override fun onIntent(intent: ProfileUiIntent) {
        when (intent) {
            is ProfileUiIntent.UpdateNickname -> handleNicknameChanged(intent.nickname)
            is ProfileUiIntent.UpdateProfileImage -> handleProfileImageChanged(
                profileUrl = intent.profileUrl,
                profileImageFile = intent.profileImageFile,
            )

            ProfileUiIntent.ClearProfileImage -> handleProfileImageChanged(
                profileUrl = "",
                profileImageFile = null,
            )

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

        val sanitizedNickname = nickname.sanitizeNickname()
        if (sanitizedNickname == uiState.editing.nickname) return

        updateState { uiState.updateNickname(sanitizedNickname) }

        nicknameChanges.value = sanitizedNickname
    }

    private fun handleProfileImageChanged(
        profileUrl: String,
        profileImageFile: File?,
    ) {
        val uiState = currentState as? ProfileUiState.Content ?: return
        if (profileUrl == uiState.editing.profileImageUrl.orEmpty()) return

        updateState {
            uiState.copy(
                editing = uiState.editing.copy(
                    profileImageUrl = profileUrl.ifBlank { null },
                    profileImageFile = profileImageFile,
                ),
            )
        }
    }

    private fun submitProfile() {
        val uiState = currentState as? ProfileUiState.Content ?: return
        if (!uiState.submitButtonEnabled) return

        viewModelScope.launch {
            patchUserProfileUseCase(
                nickname = uiState.editing.nickname,
                profileImageFile = uiState.editing.profileImageFile,
            ).onSuccess {
                if (uiState.isRegistered) return@launch sendEffect(ProfileUiEffect.NavigateToBack)
                sendEffect(ProfileUiEffect.NavigateToHome)
            }.onFailure { throwable ->
                Timber.e(throwable)
                sendEffect(ProfileUiEffect.ShowMessage(ProfileUiMessage.PATCH_USER_PROFILE_FAILED))
            }
        }
    }

    private suspend fun validateNickname(nickname: String) {
        val uiState = currentState as? ProfileUiState.Content ?: return
        if (nickname.isBlank() || !uiState.isNicknameChanged) return

        val validationState = validateNicknameState(nickname)

        updateState {
            val uiState = currentState as? ProfileUiState.Content ?: return@updateState currentState
            if (uiState.editing.nickname != nickname) return@updateState currentState
            uiState.updateNicknameValidation(validationState)
        }
    }

    private suspend fun validateNicknameState(nickname: String): NicknameValidationState =
        when (val result = validateNicknameUseCase(nickname)) {
            is ValidateNicknameUseCase.Result.Available -> NicknameValidationState.Available
            is ValidateNicknameUseCase.Result.Invalid.Format -> result.reason.toValidationState()
            is ValidateNicknameUseCase.Result.Invalid.Duplicated -> NicknameValidationState.Duplicated
            is ValidateNicknameUseCase.Result.Error -> {
                sendEffect(ProfileUiEffect.ShowMessage(ProfileUiMessage.CHECK_NICKNAME_FAILED))
                NicknameValidationState.Unchecked
            }
        }

    private fun Nickname.InvalidReason.toValidationState(): NicknameValidationState =
        when (this) {
            Nickname.InvalidReason.TOO_SHORT -> NicknameValidationState.TooShort
            Nickname.InvalidReason.TOO_LONG -> NicknameValidationState.TooLong
            Nickname.InvalidReason.INVALID_CHARACTER -> NicknameValidationState.InvalidCharacter
        }

    private fun String.sanitizeNickname(): String =
        filterNot(Char::isWhitespace)
            .take(Nickname.MAX_LENGTH)

    private fun ProfileUiState.Content.updateNickname(nickname: String): ProfileUiState.Content {
        val updatedState = copy(editing = editing.copy(nickname = nickname))

        return updatedState.updateNicknameValidation(
            validationState = updatedState.resolveNicknameValidationState(previousNickname = editing.nickname),
        )
    }

    private fun ProfileUiState.Content.resolveNicknameValidationState(previousNickname: String): NicknameValidationState =
        when {
            !isNicknameChanged -> NicknameValidationState.Unchecked
            editing.nickname.isBlank() && previousNickname.isNotBlank() -> NicknameValidationState.TooShort
            editing.nickname.isBlank() -> NicknameValidationState.Unchecked
            else -> NicknameValidationState.Checking
        }

    private fun ProfileUiState.Content.updateNicknameValidation(validationState: NicknameValidationState): ProfileUiState.Content =
        copy(editing = editing.copy(nicknameValidation = validationState))

    private companion object {
        const val NICKNAME_VALIDATION_DEBOUNCE_MILLIS = 300L
    }
}
