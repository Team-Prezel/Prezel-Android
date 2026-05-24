package com.team.prezel.core.model.presentation

data class PresentationRecordingAnalysisResult(
    val presentationId: Long,
    val analysisResultId: Long,
    val name: String,
    val type: String,
    val purpose: String,
    val style: String,
    val audience: String,
    val analysisDate: String,
    val durationSeconds: Int,
    val formattedDuration: String,
    val spm: Int,
    val speedEval: String,
    val summaryFeedback: String,
    val accuracyScore: Double,
    val scriptMatchRate: Double,
    val spellErrorCount: Int,
    val grammarErrorCount: Int,
    val totalErrorCount: Int,
    val growthGraph: List<PresentationGrowthGraph>,
    val expectedQuestions: List<PresentationExpectedQuestion>,
)

data class PresentationGrowthGraph(
    val attempt: Int,
    val accuracyScore: Double,
    val scriptMatchRate: Double,
)

data class PresentationExpectedQuestion(
    val question: String,
    val answer: String,
)
