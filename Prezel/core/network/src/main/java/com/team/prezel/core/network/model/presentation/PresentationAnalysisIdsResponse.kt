package com.team.prezel.core.network.model.presentation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresentationAnalysisIdsResponse(
    @SerialName("presentationId")
    val presentationId: Long,
    @SerialName("analysisResultId")
    val analysisResultId: Long,
)
