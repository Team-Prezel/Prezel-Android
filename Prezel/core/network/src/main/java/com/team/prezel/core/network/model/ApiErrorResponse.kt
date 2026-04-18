package com.team.prezel.core.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ApiErrorResponse(
    val status: Int,
    val code: String,
    val data: JsonElement? = null,
    val message: String,
)
