package com.team.prezel.core.network.model.auth.reissue

import com.team.prezel.core.model.auth.AuthTokens
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReissueRequest(
    @SerialName("refresh-token")
    val refreshToken: String,
)
