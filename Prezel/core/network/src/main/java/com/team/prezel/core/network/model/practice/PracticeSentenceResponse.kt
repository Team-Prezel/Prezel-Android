package com.team.prezel.core.network.model.practice

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PracticeSentenceResponse(
    @SerialName("sentence")
    val sentence: String,
)
