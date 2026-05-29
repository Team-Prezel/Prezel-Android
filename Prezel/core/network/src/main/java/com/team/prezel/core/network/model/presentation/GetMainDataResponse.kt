package com.team.prezel.core.network.model.presentation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMainDataResponse(
    @SerialName("accuracyScoreChange")
    val accuracyScoreChange: Int,
    @SerialName("dday")
    val dDay: String,
    @SerialName("growthGraph")
    val growthGraph: List<GrowthGraph>,
    @SerialName("isPast")
    val isPast: Boolean,
    @SerialName("presentationDate")
    val presentationDate: String,
    @SerialName("presentationId")
    val presentationId: Int,
    @SerialName("scriptMatchRateChange")
    val scriptMatchRateChange: Int,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String,
) {
    @Serializable
    data class GrowthGraph(
        @SerialName("accuracyScore")
        val accuracyScore: Double,
        @SerialName("attempt")
        val attempt: Int,
        @SerialName("scriptMatchRate")
        val scriptMatchRate: Double,
    )
}
