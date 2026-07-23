package com.team.prezel.feature.badge.impl.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface BadgeUiIntent : UiIntent {
    data class FetchBadgeDetail(
        val badgeCode: String,
    ) : BadgeUiIntent
}
