package com.team.prezel.core.network.model.terms

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgreeTermsRequest(
    @SerialName("termsId")
    val termsId: Long,
    @SerialName("isAgreed")
    val isAgreed: Boolean,
)
