package com.team.prezel.feature.profile.impl.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.profile.impl.model.ProfileUiMessage
import com.team.prezel.feature.profile.impl.model.ProfileUpdateResult

internal sealed interface ProfileUiEffect : UiEffect {
    data object NavigateToHome : ProfileUiEffect

    data class NavigateToBack(
        val result: ProfileUpdateResult? = null,
    ) : ProfileUiEffect

    data class ShowMessage(
        val message: ProfileUiMessage,
    ) : ProfileUiEffect
}
