package com.team.prezel.core.model.presentation

import com.team.prezel.core.model.practice.RecordingSpeed

data class PresentationAnalysisSummary(
    val presentationId: Long,
    val analysisResultId: Long,
    val title: String,
    val category: Category,
    val purpose: Purpose,
    val style: Style,
    val audience: Audience,
    val analyzedAt: String,
    val durationSeconds: Int,
    val formattedDuration: String,
    val spm: Int,
    val speedEvaluation: RecordingSpeed,
    val summaryFeedback: String,
    val accuracyScore: Double?,
    val scriptMatchRate: Double?,
    val spellErrorCount: Int,
    val grammarErrorCount: Int,
    val totalErrorCount: Int,
    val growth: List<PresentationGrowthPoint>,
    val expectedQuestions: List<ExpectedQuestion>,
    val selfFeedback: String?,
)

data class PresentationGrowthPoint(
    val attempt: Int,
    val accuracyScore: Double,
    val scriptMatchRate: Double,
)

data class ExpectedQuestion(
    val question: String,
    val answer: String,
)
