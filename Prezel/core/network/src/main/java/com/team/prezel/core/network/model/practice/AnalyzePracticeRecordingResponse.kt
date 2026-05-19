package com.team.prezel.core.network.model.practice

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnalyzePracticeRecordingResponse(
    @SerialName("accuracyScore")
    val accuracyScore: Double,
    @SerialName("speedEvaluation")
    val speedEvaluation: String,
    @SerialName("overallEvaluation")
    val overallEvaluation: String,
)
