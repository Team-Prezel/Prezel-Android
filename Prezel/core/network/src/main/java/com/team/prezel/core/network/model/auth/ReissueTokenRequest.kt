package com.team.prezel.core.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReissueTokenRequest(
    @SerialName("refresh-token")
    val refreshToken: String,
)
