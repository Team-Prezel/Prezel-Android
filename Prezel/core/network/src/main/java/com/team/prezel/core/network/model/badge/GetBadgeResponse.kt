package com.team.prezel.core.network.model.badge

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetBadgeResponse(
    @SerialName("badgeCode")
    val badgeCode: String,
    @SerialName("badgeName")
    val badgeName: String,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("isUnlocked")
    val isUnlocked: Boolean,
    @SerialName("unlockedAt")
    val unlockedAt: String,
)
