package com.team.prezel.core.data.mapper

import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.ExpectedQuestion
import com.team.prezel.core.model.presentation.PresentationAnalysisRequest
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import com.team.prezel.core.model.presentation.PresentationGrowthPoint
import com.team.prezel.core.model.presentation.PresentationScriptDetail
import com.team.prezel.core.model.presentation.PresentationWordDetail
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.ScriptCorrection
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.model.presentation.WordAnalysisDetail
import com.team.prezel.core.network.model.presentation.PresentationAnalysisRequestParts
import com.team.prezel.core.network.model.presentation.PresentationExpectedQuestionResponse
import com.team.prezel.core.network.model.presentation.PresentationGrowthResponse
import com.team.prezel.core.network.model.presentation.PresentationScriptAnalysisResponse
import com.team.prezel.core.network.model.presentation.PresentationScriptDetailResponse
import com.team.prezel.core.network.model.presentation.PresentationSummaryResponse
import com.team.prezel.core.network.model.presentation.PresentationWordAnalysisResponse
import com.team.prezel.core.network.model.presentation.PresentationWordDetailResponse
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Instant

internal fun PresentationAnalysisRequest.toRequestParts(): PresentationAnalysisRequestParts =
    PresentationAnalysisRequestParts(
        name = title,
        date = dateTime.toString(),
        type = category.toServerType(),
        purpose = purpose.toServerPurpose(),
        style = style.toServerStyle(),
        audience = audience.toServerAudience(),
        script = script,
        audioFilePath = audioFilePath,
    )

internal fun PresentationSummaryResponse.toDomain(): PresentationAnalysisSummary =
    PresentationAnalysisSummary(
        presentationId = presentationId,
        analysisResultId = analysisResultId,
        title = name,
        category = type.toDomainCategory(),
        purpose = purpose.toDomainPurpose(),
        style = style.toDomainStyle(),
        audience = audience.toDomainAudience(),
        analyzedAt = analysisDate.toAnalysisInstant(),
        durationSeconds = durationSeconds,
        formattedDuration = formattedDuration,
        spm = spm,
        speedEvaluation = speedEval,
        summaryFeedback = summaryFeedback,
        accuracyScore = accuracyScore,
        scriptMatchRate = scriptMatchRate,
        spellErrorCount = spellErrorCount,
        grammarErrorCount = grammarErrorCount,
        totalErrorCount = totalErrorCount,
        growth = growthGraph.map { item -> item.toDomain() },
        expectedQuestions = expectedQuestions.map { item -> item.toDomain() },
    )

internal fun PresentationGrowthResponse.toDomain(): PresentationGrowthPoint =
    PresentationGrowthPoint(
        attempt = attempt,
        accuracyScore = accuracyScore,
        scriptMatchRate = scriptMatchRate,
    )

internal fun PresentationExpectedQuestionResponse.toDomain(): ExpectedQuestion =
    ExpectedQuestion(
        question = question,
        answer = answer,
    )

internal fun PresentationScriptDetailResponse.toDomain(): PresentationScriptDetail =
    PresentationScriptDetail(
        presentationId = presentationId,
        audioUrl = audioUrl,
        originalScript = originalScript,
        scriptCorrections = scriptDetails.map { item -> item.toDomain() },
    )

internal fun PresentationScriptAnalysisResponse.toDomain(): ScriptCorrection =
    ScriptCorrection(
        errorType = errorType,
        sentence = sentence,
        originalText = originalText,
        correctedText = correctedText,
        reason = reason,
    )

internal fun PresentationWordDetailResponse.toDomain(): PresentationWordDetail =
    PresentationWordDetail(
        presentationId = presentationId,
        audioUrl = audioUrl,
        wordDetails = wordDetails.map { item -> item.toDomain() },
    )

internal fun PresentationWordAnalysisResponse.toDomain(): WordAnalysisDetail =
    WordAnalysisDetail(
        word = word,
        status = status,
        description = description,
        accuracy = accuracy,
        startTimeMs = startTimeMs,
        endTimeMs = endTimeMs,
    )

private fun Category.toServerType(): String =
    when (this) {
        Category.PERSUASION -> "OFFER"
        Category.EVENT -> "EVENT"
        Category.EDUCATION -> "EDUCATION"
        Category.REPORT -> "WORK"
    }

private fun Purpose.toServerPurpose(): String =
    when (this) {
        Purpose.CONTENT_DELIVERY -> "INFO"
        Purpose.IMPROVE_UNDERSTANDING -> "UNDERSTANDING"
        Purpose.BUILD_EMPATHY -> "EMPATHY"
    }

private fun Style.toServerStyle(): String =
    when (this) {
        Style.PROFESSIONAL -> "FORMAL"
        Style.FRIENDLY -> "FRIENDLY"
        Style.CALM -> "CALM"
        Style.COMFORTABLE -> "CASUAL"
    }

private fun Audience.toServerAudience(): String =
    when (this) {
        Audience.GENERAL_AUDIENCE -> "GENERAL"
        Audience.EXPERT -> "PROFESSIONAL"
        Audience.TEAMMATES -> "TEAMMATE"
    }

private fun String.toDomainCategory(): Category =
    when (this) {
        "OFFER" -> Category.PERSUASION
        "EVENT" -> Category.EVENT
        "EDUCATION" -> Category.EDUCATION
        "WORK" -> Category.REPORT
        else -> Category.REPORT
    }

private fun String.toDomainPurpose(): Purpose =
    when (this) {
        "INFO" -> Purpose.CONTENT_DELIVERY
        "UNDERSTANDING" -> Purpose.IMPROVE_UNDERSTANDING
        "EMPATHY" -> Purpose.BUILD_EMPATHY
        else -> Purpose.CONTENT_DELIVERY
    }

private fun String.toDomainStyle(): Style =
    when (this) {
        "FORMAL" -> Style.PROFESSIONAL
        "FRIENDLY" -> Style.FRIENDLY
        "CALM" -> Style.CALM
        "CASUAL" -> Style.COMFORTABLE
        else -> Style.PROFESSIONAL
    }

private fun String.toDomainAudience(): Audience =
    when (this) {
        "GENERAL" -> Audience.GENERAL_AUDIENCE
        "PROFESSIONAL" -> Audience.EXPERT
        "TEAMMATE" -> Audience.TEAMMATES
        else -> Audience.GENERAL_AUDIENCE
    }

private fun String.toAnalysisInstant(): Instant =
    runCatching { Instant.parse(this) }
        .getOrElse {
            LocalDateTime
                .parse(this)
                .toInstant(TimeZone.of("Asia/Seoul"))
        }
