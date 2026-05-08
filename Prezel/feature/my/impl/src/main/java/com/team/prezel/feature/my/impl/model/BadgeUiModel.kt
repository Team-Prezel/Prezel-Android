package com.team.prezel.feature.my.impl.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.badge.Badge
import com.team.prezel.core.model.badge.BadgeType

@Immutable
internal data class BadgeUiModel(
    val type: BadgeType,
    val isAchieved: Boolean,
)

internal fun Badge.toUiModel(): BadgeUiModel =
    BadgeUiModel(
        type = type,
        isAchieved = isAchieved,
    )
