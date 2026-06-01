package com.team.prezel.feature.badge.impl.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class BadgeDetailUiModel(
    val badgeCode: String,
    val badgeName: String,
    val conditionText: String,
    val detailDescription: String,
    val imageUrl: String,
    val isUnlocked: Boolean,
)
