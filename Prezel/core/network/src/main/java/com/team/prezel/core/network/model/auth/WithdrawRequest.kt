package com.team.prezel.core.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WithdrawRequest(
    @SerialName("reasonCategory")
    val reasonCategory: String,
    @SerialName("reasonText")
    val reasonText: String,
)
