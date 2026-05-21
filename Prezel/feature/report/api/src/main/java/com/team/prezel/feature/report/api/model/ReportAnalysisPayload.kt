package com.team.prezel.feature.report.api.model

import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.ExpectedQuestion
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import com.team.prezel.core.model.presentation.PresentationGrowthPoint
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import kotlinx.serialization.Serializable

@Serializable
data class ReportAnalysisPayload(
    val presentationId: Long,
    val analysisResultId: Long,
    val presentationInfo: ReportPresentationInfoPayload,
    val durationSeconds: Int,
    val formattedDuration: String,
    val spm: Int,
    val speedEval: ReportSpeedEvalPayload,
    val summaryFeedback: String,
    val accuracyScore: Double,
    val scriptMatchRate: Double,
    val spellErrorCount: Int,
    val grammarErrorCount: Int,
    val totalErrorCount: Int,
    val growth: List<ReportGrowthPayload>,
    val expectedQuestions: List<ReportExpectedQuestionPayload>,
)

@Serializable
data class ReportPresentationInfoPayload(
    val category: ReportCategoryPayload,
    val title: String,
    val purpose: ReportPurposePayload,
    val style: ReportStylePayload,
    val audience: ReportAudiencePayload,
    val analyzedAt: String,
)

@Serializable
data class ReportGrowthPayload(
    val attempt: Int,
    val accuracyScore: Double,
    val scriptMatchRate: Double,
)

@Serializable
data class ReportExpectedQuestionPayload(
    val question: String,
    val answer: String,
)

@Serializable
enum class ReportSpeedEvalPayload {
    SLOW,
    ADEQUATE,
    FAST,
}

@Serializable
enum class ReportCategoryPayload {
    PERSUASION,
    EVENT,
    EDUCATION,
    REPORT,
}

@Serializable
enum class ReportPurposePayload {
    CONTENT_DELIVERY,
    IMPROVE_UNDERSTANDING,
    BUILD_EMPATHY,
}

@Serializable
enum class ReportStylePayload {
    PROFESSIONAL,
    FRIENDLY,
    CALM,
    COMFORTABLE,
}

@Serializable
enum class ReportAudiencePayload {
    GENERAL_AUDIENCE,
    EXPERT,
    TEAMMATES,
}

fun PresentationAnalysisSummary.toReportAnalysisPayload(): ReportAnalysisPayload =
    ReportAnalysisPayload(
        presentationId = presentationId,
        analysisResultId = analysisResultId,
        presentationInfo = ReportPresentationInfoPayload(
            category = category.toPayload(),
            title = title,
            purpose = purpose.toPayload(),
            style = style.toPayload(),
            audience = audience.toPayload(),
            analyzedAt = analyzedAt.toString(),
        ),
        durationSeconds = durationSeconds,
        formattedDuration = formattedDuration,
        spm = spm,
        speedEval = speedEvaluation.toPayload(),
        summaryFeedback = summaryFeedback,
        accuracyScore = accuracyScore,
        scriptMatchRate = scriptMatchRate,
        spellErrorCount = spellErrorCount,
        grammarErrorCount = grammarErrorCount,
        totalErrorCount = totalErrorCount,
        growth = growth.map(PresentationGrowthPoint::toPayload),
        expectedQuestions = expectedQuestions.map(ExpectedQuestion::toPayload),
    )

private fun PresentationGrowthPoint.toPayload(): ReportGrowthPayload =
    ReportGrowthPayload(
        attempt = attempt,
        accuracyScore = accuracyScore,
        scriptMatchRate = scriptMatchRate,
    )

private fun ExpectedQuestion.toPayload(): ReportExpectedQuestionPayload =
    ReportExpectedQuestionPayload(
        question = question,
        answer = answer,
    )

private fun Category.toPayload(): ReportCategoryPayload =
    when (this) {
        Category.PERSUASION -> ReportCategoryPayload.PERSUASION
        Category.EVENT -> ReportCategoryPayload.EVENT
        Category.EDUCATION -> ReportCategoryPayload.EDUCATION
        Category.REPORT -> ReportCategoryPayload.REPORT
    }

private fun Purpose.toPayload(): ReportPurposePayload =
    when (this) {
        Purpose.CONTENT_DELIVERY -> ReportPurposePayload.CONTENT_DELIVERY
        Purpose.IMPROVE_UNDERSTANDING -> ReportPurposePayload.IMPROVE_UNDERSTANDING
        Purpose.BUILD_EMPATHY -> ReportPurposePayload.BUILD_EMPATHY
    }

private fun Style.toPayload(): ReportStylePayload =
    when (this) {
        Style.PROFESSIONAL -> ReportStylePayload.PROFESSIONAL
        Style.FRIENDLY -> ReportStylePayload.FRIENDLY
        Style.CALM -> ReportStylePayload.CALM
        Style.COMFORTABLE -> ReportStylePayload.COMFORTABLE
    }

private fun Audience.toPayload(): ReportAudiencePayload =
    when (this) {
        Audience.GENERAL_AUDIENCE -> ReportAudiencePayload.GENERAL_AUDIENCE
        Audience.EXPERT -> ReportAudiencePayload.EXPERT
        Audience.TEAMMATES -> ReportAudiencePayload.TEAMMATES
    }

private fun String.toPayload(): ReportSpeedEvalPayload =
    when {
        contains("느려요") -> ReportSpeedEvalPayload.SLOW
        contains("빨라요") -> ReportSpeedEvalPayload.FAST
        else -> ReportSpeedEvalPayload.ADEQUATE
    }
