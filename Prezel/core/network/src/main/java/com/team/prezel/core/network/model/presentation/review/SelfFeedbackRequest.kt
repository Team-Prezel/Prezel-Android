package com.team.prezel.core.network.model.presentation.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SelfFeedbackRequest(
    @SerialName("content")
    val content: String,
)
