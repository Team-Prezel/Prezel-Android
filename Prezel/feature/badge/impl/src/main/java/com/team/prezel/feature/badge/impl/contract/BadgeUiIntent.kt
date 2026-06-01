package com.team.prezel.feature.badge.impl.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface BadgeUiIntent : UiIntent {
    data class ClickBadge(
        val badgeCode: String,
    ) : BadgeUiIntent

    data object DismissBadgeDetail : BadgeUiIntent
}
