package com.team.prezel.core.network.model.badge

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BadgeEventResponse(
    @SerialName("badgeCode")
    val badgeCode: String? = null,
    @SerialName("badgeName")
    val badgeName: String? = null,
    @SerialName("introduction")
    val introduction: String? = null,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("message")
    val message: String? = null,
    val rawData: String? = null,
)
