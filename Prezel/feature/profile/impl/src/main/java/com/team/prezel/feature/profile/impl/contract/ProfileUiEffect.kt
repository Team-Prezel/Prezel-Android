package com.team.prezel.feature.profile.impl.contract

import com.team.prezel.core.ui.UiEffect
import com.team.prezel.feature.profile.impl.model.ProfileUiMessage

internal sealed interface ProfileUiEffect : UiEffect {
    data object NavigateToHome : ProfileUiEffect

    data object OnBack : ProfileUiEffect

    data class ShowMessage(
        val message: ProfileUiMessage,
    ) : ProfileUiEffect
}
