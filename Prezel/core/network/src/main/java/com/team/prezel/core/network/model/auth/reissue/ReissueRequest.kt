package com.team.prezel.core.network.model.auth.reissue

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReissueRequest(
    @SerialName("refresh-token")
    val refreshToken: String,
)
