package com.team.prezel.feature.badge.impl.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.badge.impl.model.BadgeUiMessage

internal sealed interface BadgeUiEffect : UiEffect {
    data class ShowMessage(
        val message: BadgeUiMessage,
    ) : BadgeUiEffect
}
