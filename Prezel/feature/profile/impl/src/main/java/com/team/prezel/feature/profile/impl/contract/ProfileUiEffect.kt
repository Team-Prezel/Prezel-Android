package com.team.prezel.feature.profile.impl.contract

import com.team.prezel.core.ui.UiEffect
import com.team.prezel.feature.profile.impl.model.ProfileUiMessage

sealed interface ProfileUiEffect : UiEffect {
    data object NavigateToLogin : ProfileUiEffect

    data class ShowMessage(
        val message: ProfileUiMessage,
    ) : ProfileUiEffect
}
