package com.team.prezel.core.network.model.presentation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresentationWordDetailResponse(
    @SerialName("presentationId")
    val presentationId: Long,
    @SerialName("audioUrl")
    val audioUrl: String,
    @SerialName("wordDetails")
    val wordDetails: List<PresentationWordAnalysisResponse>,
)

@Serializable
data class PresentationWordAnalysisResponse(
    @SerialName("word")
    val word: String,
    @SerialName("status")
    val status: String,
    @SerialName("description")
    val description: String,
    @SerialName("accuracy")
    val accuracy: Double,
    @SerialName("startTimeMs")
    val startTimeMs: Long,
    @SerialName("endTimeMs")
    val endTimeMs: Long,
)
