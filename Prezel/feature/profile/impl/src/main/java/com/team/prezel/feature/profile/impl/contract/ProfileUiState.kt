package com.team.prezel.feature.profile.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.ui.UiState

@Immutable
internal sealed interface ProfileUiState : UiState {
    fun canPhotoPickerLaunch(): Boolean = (this as? Fetched)?.profileImage?.isDefault == true

    data object Loading : ProfileUiState

    interface Fetched {
        val nickname: String
        val nicknameValidation: NicknameValidationState
        val profileImage: User.ProfileImage

        val submitButtonEnabled: Boolean

        fun updateProfile(
            nickname: String = this.nickname,
            nicknameValidation: NicknameValidationState = this.nicknameValidation,
            profileImage: User.ProfileImage = this.profileImage,
        ): ProfileUiState
    }

    data class Create(
        override val nickname: String = "",
        override val nicknameValidation: NicknameValidationState = NicknameValidationState.Unchecked,
        override val profileImage: User.ProfileImage = User.ProfileImage(url = "", isDefault = true),
    ) : Fetched,
        ProfileUiState {
        override val submitButtonEnabled: Boolean = nicknameValidation == NicknameValidationState.Available

        override fun updateProfile(
            nickname: String,
            nicknameValidation: NicknameValidationState,
            profileImage: User.ProfileImage,
        ): ProfileUiState =
            copy(
                nickname = nickname,
                nicknameValidation = nicknameValidation,
                profileImage = profileImage,
            )
    }

    data class Edit(
        val originalNickname: String,
        override val nickname: String = originalNickname,
        override val nicknameValidation: NicknameValidationState = NicknameValidationState.Available,
        val originalProfileImage: User.ProfileImage,
        override val profileImage: User.ProfileImage,
    ) : Fetched,
        ProfileUiState {
        override val submitButtonEnabled: Boolean =
            (nicknameValidation == NicknameValidationState.Available && nickname != originalNickname) ||
                profileImage != originalProfileImage

        override fun updateProfile(
            nickname: String,
            nicknameValidation: NicknameValidationState,
            profileImage: User.ProfileImage,
        ): ProfileUiState =
            copy(
                nickname = nickname,
                nicknameValidation = nicknameValidation,
                profileImage = profileImage,
            )
    }

    companion object {
        fun User.toUiState(): ProfileUiState =
            if (profileImage.isDefault) {
                Create(
                    nickname = nickname,
                    profileImage = profileImage,
                )
            } else {
                Edit(
                    originalNickname = nickname,
                    nickname = nickname,
                    originalProfileImage = profileImage,
                    profileImage = profileImage,
                )
            }
    }
}

internal enum class NicknameValidationState {
    Unchecked,
    Checking,
    Available,
    TooShort,
    TooLong,
    InvalidCharacter,
    Duplicated,
}
