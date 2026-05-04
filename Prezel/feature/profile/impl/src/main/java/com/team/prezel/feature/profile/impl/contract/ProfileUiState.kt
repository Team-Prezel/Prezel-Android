package com.team.prezel.feature.profile.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.profile.impl.model.NicknameValidationState

@Immutable
internal sealed interface ProfileUiState : UiState {
    val shouldLaunchPhotoPicker get(): Boolean = (this as? Content)?.profileImageUrl.isNullOrBlank()

    data object Loading : ProfileUiState

    data class Content(
        private val originalNickname: String,
        private val originalProfileImageUrl: String?,
        val nickname: String,
        val nicknameValidation: NicknameValidationState,
        val profileImageUrl: String?,
    ) : ProfileUiState {
        val submitButtonEnabled: Boolean =
            nicknameValidation == NicknameValidationState.Available &&
                (nickname != originalNickname || profileImageUrl != originalProfileImageUrl)

        companion object {
            fun User.toUiState(): ProfileUiState =
                Content(
                    originalNickname = nickname,
                    originalProfileImageUrl = profileImageUrl,
                    nickname = nickname,
                    nicknameValidation = if (isRegistered) NicknameValidationState.Available else NicknameValidationState.Unchecked,
                    profileImageUrl = profileImageUrl,
                )
        }
    }
}
