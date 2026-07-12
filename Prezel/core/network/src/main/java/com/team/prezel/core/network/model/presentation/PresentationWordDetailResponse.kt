package com.team.prezel.core.network.model.presentation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresentationWordDetailResponse(
    @SerialName("presentationId")
    val presentationId: Long,
    @SerialName("audioUrl")
    val audioUrl: String,
    @SerialName("sentenceDetails")
    val sentenceDetails: List<PresentationSentenceAnalysisResponse>,
)

@Serializable
data class PresentationSentenceAnalysisResponse(
    @SerialName("sentence")
    val sentence: String,
    @SerialName("status")
    val status: String,
    @SerialName("mainFeedback")
    val mainFeedback: String,
    @SerialName("subFeedback")
    val subFeedback: String,
    @SerialName("guideScript")
    val guideScript: String = "",
    @SerialName("accuracy")
    val accuracy: Double,
    @SerialName("startTimeMs")
    val startTimeMs: Long,
    @SerialName("endTimeMs")
    val endTimeMs: Long,
    @SerialName("wordDetails")
    val wordDetails: List<PresentationWordAnalysisResponse>,
)

@Serializable
data class PresentationWordAnalysisResponse(
    @SerialName("word")
    val word: String,
    @SerialName("status")
    val status: String,
    @SerialName("accuracy")
    val accuracy: Double,
    @SerialName("startTimeMs")
    val startTimeMs: Long,
    @SerialName("endTimeMs")
    val endTimeMs: Long,
)
