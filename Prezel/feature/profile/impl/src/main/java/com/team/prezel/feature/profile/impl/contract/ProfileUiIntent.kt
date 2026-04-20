package com.team.prezel.feature.profile.impl.contract

import com.team.prezel.core.ui.UiIntent

internal sealed interface ProfileUiIntent : UiIntent {
    data object FetchData : ProfileUiIntent

    data class UpdateNickname(
        val nickname: String,
    ) : ProfileUiIntent

    data class UpdateProfileImage(
        val profileUrl: String,
    ) : ProfileUiIntent

    data object SubmitProfile : ProfileUiIntent
}
