package com.team.prezel.core.network.model.presentation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresentationScriptDetailResponse(
    @SerialName("presentationId")
    val presentationId: Long,
    @SerialName("audioUrl")
    val audioUrl: String,
    @SerialName("originalScript")
    val originalScript: String,
    @SerialName("scriptDetails")
    val scriptDetails: List<PresentationScriptAnalysisResponse>?,
)

@Serializable
data class PresentationScriptAnalysisResponse(
    @SerialName("errorType")
    val errorType: String,
    @SerialName("sentence")
    val sentence: String,
    @SerialName("originalText")
    val originalText: String,
    @SerialName("correctedText")
    val correctedText: String,
    @SerialName("reason")
    val reason: String,
    @SerialName("startIndex")
    val startIndex: Int,
    @SerialName("endIndex")
    val endIndex: Int,
)
