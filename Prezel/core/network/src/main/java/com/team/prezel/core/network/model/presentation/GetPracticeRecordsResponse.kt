package com.team.prezel.core.network.model.presentation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPracticeRecordsResponse(
    @SerialName("dates")
    val dates: List<String>,
    @SerialName("endDate")
    val endDate: String,
    @SerialName("startDate")
    val startDate: String,
)
