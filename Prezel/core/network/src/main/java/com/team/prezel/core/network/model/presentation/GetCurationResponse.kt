package com.team.prezel.core.network.model.presentation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCurationResponse(
    @SerialName("guideMessage")
    val guideMessage: String,
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
