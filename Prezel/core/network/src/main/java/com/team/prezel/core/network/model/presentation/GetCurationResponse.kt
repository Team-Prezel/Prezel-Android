package com.team.prezel.core.network.model.presentation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCurationResponse(
    @SerialName("guideMessage")
    val guideMessage: String,
    @SerialName("type")
    val type: String,
    @SerialName("purpose")
    val purpose: String,
    @SerialName("style")
    val style: String,
    @SerialName("audience")
    val audience: String,
    @SerialName("materialType")
    val materialType: String,
    @SerialName("title")
    val title: String,
    @SerialName("sourceChannel")
    val sourceChannel: String,
    @SerialName("linkUrl")
    val linkUrl: String,
    @SerialName("imageUrl")
    val imageUrl: String,
)
