package com.team.prezel.core.network.model.presentation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresentationSummaryResponse(
    @SerialName("presentationId")
    val presentationId: Long,
    @SerialName("analysisResultId")
    val analysisResultId: Long,
    @SerialName("name")
    val name: String,
    @SerialName("type")
    val type: String,
    @SerialName("purpose")
    val purpose: String,
    @SerialName("style")
    val style: String,
    @SerialName("audience")
    val audience: String,
    @SerialName("analysisDate")
    val analysisDate: String,
    @SerialName("durationSeconds")
    val durationSeconds: Int,
    @SerialName("formattedDuration")
    val formattedDuration: String,
    @SerialName("spm")
    val spm: Int,
    @SerialName("speedEval")
    val speedEval: String,
    @SerialName("summaryFeedback")
    val summaryFeedback: String,
    @SerialName("accuracyScore")
    val accuracyScore: Double,
    @SerialName("scriptMatchRate")
    val scriptMatchRate: Double,
    @SerialName("spellErrorCount")
    val spellErrorCount: Int,
    @SerialName("grammarErrorCount")
    val grammarErrorCount: Int,
    @SerialName("totalErrorCount")
    val totalErrorCount: Int,
    @SerialName("growthGraph")
    val growthGraph: List<PresentationGrowthResponse>,
    @SerialName("expectedQuestions")
    val expectedQuestions: List<PresentationExpectedQuestionResponse>,
)

@Serializable
data class PresentationGrowthResponse(
    @SerialName("attempt")
    val attempt: Int,
    @SerialName("accuracyScore")
    val accuracyScore: Double,
    @SerialName("scriptMatchRate")
    val scriptMatchRate: Double,
)

@Serializable
data class PresentationExpectedQuestionResponse(
    @SerialName("question")
    val question: String,
    @SerialName("answer")
    val answer: String,
)
