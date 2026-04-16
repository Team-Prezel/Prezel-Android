package com.team.prezel.feature.profile.impl.contract

import com.team.prezel.core.ui.UiIntent

internal sealed interface ProfileUiIntent : UiIntent {
    data class OnNicknameChanged(
        val nickname: String,
    ) : ProfileUiIntent

    data object OnClickSubmit : ProfileUiIntent
}
