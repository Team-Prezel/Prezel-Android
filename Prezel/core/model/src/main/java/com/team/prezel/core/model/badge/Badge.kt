package com.team.prezel.core.model.badge

data class Badge(
    val badgeCode: String,
    val badgeName: String,
    val isUnlocked: Boolean,
    val imageUrl: String,
    val unlockedAt: String? = null,
)

data class BadgeDetail(
    val badgeCode: String,
    val badgeName: String,
    val conditionText: String,
    val detailDescription: String,
    val imageUrl: String,
    val isUnlocked: Boolean,
    val unlockedAt: String? = null,
)

data class BadgeEvent(
    val badgeCode: String? = null,
    val badgeName: String? = null,
    val message: String? = null,
    val rawData: String? = null,
)
