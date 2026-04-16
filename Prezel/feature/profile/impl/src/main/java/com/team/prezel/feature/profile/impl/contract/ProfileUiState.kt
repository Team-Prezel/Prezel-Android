package com.team.prezel.feature.profile.impl.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.ui.UiState

@Immutable
internal sealed interface ProfileUiState : UiState {
    val nickname: String
    val nicknameValidation: NicknameValidationState
    val isPrimaryActionEnabled: Boolean

    fun updateProfile(
        nickname: String = this.nickname,
        nicknameValidation: NicknameValidationState = this.nicknameValidation,
    ): ProfileUiState

    data class Create(
        override val nickname: String = "",
        override val nicknameValidation: NicknameValidationState = NicknameValidationState.Unchecked,
    ) : ProfileUiState {
        override val isPrimaryActionEnabled: Boolean = nicknameValidation == NicknameValidationState.Available

        override fun updateProfile(
            nickname: String,
            nicknameValidation: NicknameValidationState,
        ): ProfileUiState =
            copy(
                nickname = nickname,
                nicknameValidation = nicknameValidation,
            )
    }

    data class Edit(
        val originalNickname: String,
        override val nickname: String = originalNickname,
        override val nicknameValidation: NicknameValidationState = NicknameValidationState.Available,
    ) : ProfileUiState {
        override val isPrimaryActionEnabled: Boolean =
            nicknameValidation == NicknameValidationState.Available && nickname != originalNickname

        override fun updateProfile(
            nickname: String,
            nicknameValidation: NicknameValidationState,
        ): ProfileUiState =
            copy(
                nickname = nickname,
                nicknameValidation = nicknameValidation,
            )
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
