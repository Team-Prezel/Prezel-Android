package com.team.prezel.core.network.model.presentation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUpcomingPresentationsV2Response(
    @SerialName("analysisResultId")
    val analysisResultId: Long?,
    @SerialName("audioUrl")
    val audioUrl: String?,
    @SerialName("cardStatus")
    val cardStatus: String = "default",
    @SerialName("dDay")
    val dDay: String,
    @SerialName("errorType")
    val errorType: String?,
    @SerialName("presentationDate")
    val presentationDate: String,
    @SerialName("presentationId")
    val presentationId: Long,
    @SerialName("script")
    val script: String?,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String,
)
