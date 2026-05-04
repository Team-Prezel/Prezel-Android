package com.team.prezel.feature.profile.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.profile.impl.model.NicknameValidationState
import java.io.File

@Immutable
internal sealed interface ProfileUiState : UiState {
    val shouldLaunchPhotoPicker get(): Boolean = (this as? Content)?.editing?.profileImageUrl.isNullOrBlank()

    data object Loading : ProfileUiState

    @Immutable
    data class OriginalProfile(
        val nickname: String,
        val profileImageUrl: String?,
    )

    @Immutable
    data class EditingProfile(
        val nickname: String,
        val nicknameValidation: NicknameValidationState,
        val profileImageUrl: String?,
        val profileImageFile: File?,
    )

    data class Content(
        val isRegistered: Boolean,
        val original: OriginalProfile,
        val editing: EditingProfile,
    ) : ProfileUiState {
        val isNicknameChanged: Boolean = editing.nickname != original.nickname

        val isProfileImageChanged: Boolean = editing.profileImageUrl != original.profileImageUrl

        val submitButtonEnabled: Boolean =
            isProfileImageChanged || (isNicknameChanged && editing.nicknameValidation == NicknameValidationState.Available)

        companion object {
            fun User.toUiState(): ProfileUiState =
                Content(
                    isRegistered = isRegistered,
                    original = OriginalProfile(
                        nickname = nickname,
                        profileImageUrl = profileImageUrl,
                    ),
                    editing = EditingProfile(
                        nickname = nickname,
                        nicknameValidation = NicknameValidationState.Unchecked,
                        profileImageUrl = profileImageUrl,
                        profileImageFile = null,
                    ),
                )
        }
    }
}
