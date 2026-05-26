package com.team.prezel.core.model.presentation

data class PresentationWordDetail(
    val presentationId: Long,
    val audioUrl: String,
    val wordDetails: List<WordAnalysisDetail>,
)

data class WordAnalysisDetail(
    val word: String,
    val status: String,
    val description: String,
    val accuracy: Double,
    val startTimeMs: Long,
    val endTimeMs: Long,
)
