package com.team.prezel.feature.report.impl.script.model

import com.team.prezel.core.model.presentation.ScriptErrorType

data class ScriptCorrectionUiModel(
    val id: Long,
    val errorType: ScriptErrorType,
    val sentence: String,
    val originalText: String,
    val correctedText: String,
    val reason: String,
    val originalRange: IntRange,
)
