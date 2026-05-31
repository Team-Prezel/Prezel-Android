package com.team.prezel.core.network.model.badge

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetBadgeDetailResponse(
    @SerialName("badgeCode")
    val badgeCode: String,
    @SerialName("badgeName")
    val badgeName: String,
    @SerialName("conditionText")
    val conditionText: String,
    @SerialName("detailDescription")
    val detailDescription: String,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("isUnlocked")
    val isUnlocked: Boolean,
    @SerialName("unlockedAt")
    val unlockedAt: String,
)
