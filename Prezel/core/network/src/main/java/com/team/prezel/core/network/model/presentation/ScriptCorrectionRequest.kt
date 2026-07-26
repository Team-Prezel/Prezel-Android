package com.team.prezel.core.network.model.presentation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScriptCorrectionRequest(
    @SerialName("finalScript")
    val finalScript: String,
    @SerialName("correctedIndices")
    val correctedIndices: List<Int>,
)
