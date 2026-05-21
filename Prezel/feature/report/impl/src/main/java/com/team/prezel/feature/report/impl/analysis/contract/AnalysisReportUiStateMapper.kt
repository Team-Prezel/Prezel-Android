package com.team.prezel.feature.report.impl.analysis.contract

import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.feature.report.api.model.ReportAnalysisPayload
import com.team.prezel.feature.report.api.model.ReportAudiencePayload
import com.team.prezel.feature.report.api.model.ReportCategoryPayload
import com.team.prezel.feature.report.api.model.ReportExpectedQuestionPayload
import com.team.prezel.feature.report.api.model.ReportGrowthPayload
import com.team.prezel.feature.report.api.model.ReportPresentationInfoPayload
import com.team.prezel.feature.report.api.model.ReportPurposePayload
import com.team.prezel.feature.report.api.model.ReportSpeedEvalPayload
import com.team.prezel.feature.report.api.model.ReportStylePayload
import com.team.prezel.feature.report.impl.detail.model.ImprovementGraphData
import com.team.prezel.feature.report.impl.detail.model.ImprovementGraphItemUiModel
import com.team.prezel.feature.report.impl.detail.model.PresentationInfoUiModel
import com.team.prezel.feature.report.impl.detail.model.QuestionUiModel
import com.team.prezel.feature.report.impl.detail.model.ReportDetailUiModel
import com.team.prezel.feature.report.impl.detail.model.ScriptAnalysisGraphData
import com.team.prezel.feature.report.impl.detail.model.SpeedGraphData
import com.team.prezel.feature.report.impl.detail.model.SpeedResult
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Instant

internal fun ReportAnalysisPayload.toAnalysisReportUiState(): AnalysisReportUiState =
    AnalysisReportUiState.Content(
        reportDetail = ReportDetailUiModel(
            presentationInfo = presentationInfo.toPresentationInfoUiModel(
                formattedDuration = formattedDuration,
            ),
            summaryFeedback = summaryFeedback,
            accuracyScore = accuracyScore,
            scriptMatchRate = scriptMatchRate,
            speedGraphData = SpeedGraphData(
                spm = spm,
                result = speedEval.toSpeedResult(),
            ),
            improvementGraphData = growth.toImprovementGraphData(),
            scriptAnalysisGraphData = ScriptAnalysisGraphData(
                spellingCount = spellErrorCount,
                grammarCount = grammarErrorCount,
            ),
            expectedQuestions = expectedQuestions.toQuestionUiModels(),
        ),
    )

private fun ReportPresentationInfoPayload.toPresentationInfoUiModel(formattedDuration: String): PresentationInfoUiModel =
    PresentationInfoUiModel(
        category = category.toCategory(),
        title = title,
        purpose = purpose.toPurpose(),
        style = style.toStyle(),
        audience = audience.toAudience(),
        analyzedAt = analyzedAt.toAnalysisInstant(),
        formattedDuration = formattedDuration,
    )

private fun List<ReportGrowthPayload>.toImprovementGraphData(): ImprovementGraphData =
    ImprovementGraphData(
        items = map { item ->
            ImprovementGraphItemUiModel(
                attempt = item.attempt,
                accuracyScore = item.accuracyScore,
                scriptMatchRate = item.scriptMatchRate,
            )
        }.toImmutableList(),
    )

private fun List<ReportExpectedQuestionPayload>.toQuestionUiModels(): ImmutableList<QuestionUiModel> =
    map { question ->
        QuestionUiModel(
            question = question.question,
            answer = question.answer,
        )
    }.toImmutableList()

private fun ReportCategoryPayload.toCategory(): Category =
    when (this) {
        ReportCategoryPayload.PERSUASION -> Category.PERSUASION
        ReportCategoryPayload.EVENT -> Category.EVENT
        ReportCategoryPayload.EDUCATION -> Category.EDUCATION
        ReportCategoryPayload.REPORT -> Category.REPORT
    }

private fun ReportPurposePayload.toPurpose(): Purpose =
    when (this) {
        ReportPurposePayload.CONTENT_DELIVERY -> Purpose.CONTENT_DELIVERY
        ReportPurposePayload.IMPROVE_UNDERSTANDING -> Purpose.IMPROVE_UNDERSTANDING
        ReportPurposePayload.BUILD_EMPATHY -> Purpose.BUILD_EMPATHY
    }

private fun ReportStylePayload.toStyle(): Style =
    when (this) {
        ReportStylePayload.PROFESSIONAL -> Style.PROFESSIONAL
        ReportStylePayload.FRIENDLY -> Style.FRIENDLY
        ReportStylePayload.CALM -> Style.CALM
        ReportStylePayload.COMFORTABLE -> Style.COMFORTABLE
    }

private fun ReportAudiencePayload.toAudience(): Audience =
    when (this) {
        ReportAudiencePayload.GENERAL_AUDIENCE -> Audience.GENERAL_AUDIENCE
        ReportAudiencePayload.EXPERT -> Audience.EXPERT
        ReportAudiencePayload.TEAMMATES -> Audience.TEAMMATES
    }

private fun ReportSpeedEvalPayload.toSpeedResult(): SpeedResult =
    when (this) {
        ReportSpeedEvalPayload.SLOW -> SpeedResult.SLOW
        ReportSpeedEvalPayload.ADEQUATE -> SpeedResult.ADEQUATE
        ReportSpeedEvalPayload.FAST -> SpeedResult.FAST
    }

private fun String.toAnalysisInstant(): Instant =
    runCatching { Instant.parse(this) }
        .getOrElse {
            LocalDateTime
                .parse(this)
                .toInstant(TimeZone.of("Asia/Seoul"))
        }
