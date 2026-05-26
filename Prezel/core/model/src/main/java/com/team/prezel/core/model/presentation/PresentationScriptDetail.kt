package com.team.prezel.core.model.presentation

data class PresentationScriptDetail(
    val presentationId: Long,
    val audioUrl: String,
    val originalScript: String,
    val scriptCorrections: List<ScriptCorrection>,
)

data class ScriptCorrection(
    val errorType: String,
    val sentence: String,
    val originalText: String,
    val correctedText: String,
    val reason: String,
)
