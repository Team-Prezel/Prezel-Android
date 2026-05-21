package com.team.prezel.core.network.model.presentation

data class PresentationAnalysisRequestParts(
    val name: String,
    val date: String,
    val type: String,
    val purpose: String,
    val style: String,
    val audience: String,
    val script: String?,
    val audioFilePath: String,
)
