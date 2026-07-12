package com.team.prezel.core.network.model.terms

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchTermsResponse(
    @SerialName("content")
    val content: String,
    @SerialName("isRequired")
    val isRequired: Boolean,
    @SerialName("summary")
    val summary: String,
    @SerialName("termsId")
    val termsId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("version")
    val version: String,
)
