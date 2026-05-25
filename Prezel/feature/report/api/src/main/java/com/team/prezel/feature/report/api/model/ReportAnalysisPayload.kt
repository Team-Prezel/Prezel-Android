package com.team.prezel.feature.report.api.model

import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import kotlinx.serialization.Serializable

@Serializable
data class ReportAnalysisPayload(
    val presentationId: Long,
    val analysisResultId: Long,
    val title: String,
    val category: String,
    val purpose: String,
    val style: String,
    val audience: String,
    val analyzedAt: String,
    val durationSeconds: Int,
    val formattedDuration: String,
    val spm: Int,
    val speedEvaluation: String,
    val summaryFeedback: String,
    val accuracyScore: Double,
    val scriptMatchRate: Double,
    val spellErrorCount: Int,
    val grammarErrorCount: Int,
    val totalErrorCount: Int,
    val growth: List<PresentationGrowthPointPayload>,
    val expectedQuestions: List<ExpectedQuestionPayload>,
) {
    companion object {
        fun PresentationAnalysisSummary.toPayload(): ReportAnalysisPayload =
            ReportAnalysisPayload(
                presentationId = presentationId,
                analysisResultId = analysisResultId,
                title = title,
                category = category.value,
                purpose = purpose.value,
                style = style.value,
                audience = audience.value,
                analyzedAt = analyzedAt,
                durationSeconds = durationSeconds,
                formattedDuration = formattedDuration,
                spm = spm,
                speedEvaluation = speedEvaluation.value,
                summaryFeedback = summaryFeedback,
                accuracyScore = accuracyScore,
                scriptMatchRate = scriptMatchRate,
                spellErrorCount = spellErrorCount,
                grammarErrorCount = grammarErrorCount,
                totalErrorCount = totalErrorCount,
                growth = growth.map { point ->
                    PresentationGrowthPointPayload(
                        attempt = point.attempt,
                        accuracyScore = point.accuracyScore,
                        scriptMatchRate = point.scriptMatchRate,
                    )
                },
                expectedQuestions = expectedQuestions.map { (question, answer) ->
                    ExpectedQuestionPayload(question = question, answer = answer)
                },
            )
    }
}

@Serializable
data class PresentationGrowthPointPayload(
    val attempt: Int,
    val accuracyScore: Double,
    val scriptMatchRate: Double,
)

@Serializable
data class ExpectedQuestionPayload(
    val question: String,
    val answer: String,
)
