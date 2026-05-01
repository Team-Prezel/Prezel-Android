package com.team.prezel.core.network.model.terms

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgreeTermsRequest(
    @SerialName("terms")
    val terms: List<Terms>,
) {
    @Serializable
    data class Terms(
        @SerialName("termsId")
        val termsId: Long,
        @SerialName("isAgreed")
        val isAgreed: Boolean,
    )
}
