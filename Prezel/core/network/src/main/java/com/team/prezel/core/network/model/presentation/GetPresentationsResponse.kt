package com.team.prezel.core.network.model.presentation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPresentationsResponse(
    @SerialName("audience")
    val audience: String,
    @SerialName("dday")
    val dday: String,
    @SerialName("presentationDate")
    val presentationDate: String,
    @SerialName("presentationId")
    val presentationId: Long,
    @SerialName("purpose")
    val purpose: String,
    @SerialName("style")
    val style: String,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String,
)
