package com.team.prezel.core.network.model.presentation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPresentationDetailResponse(
    @SerialName("analysisResult")
    val analysisResult: PresentationSummaryResponse,
)
