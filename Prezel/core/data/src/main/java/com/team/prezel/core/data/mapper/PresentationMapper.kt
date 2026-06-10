package com.team.prezel.core.data.mapper

import com.team.prezel.core.model.practice.RecordingSpeed
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.ExpectedQuestion
import com.team.prezel.core.model.presentation.MainData
import com.team.prezel.core.model.presentation.PracticeRecords
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import com.team.prezel.core.model.presentation.PresentationGrowthPoint
import com.team.prezel.core.model.presentation.PresentationInfo
import com.team.prezel.core.model.presentation.PresentationScriptDetail
import com.team.prezel.core.model.presentation.PresentationWordDetail
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.ScriptCorrection
import com.team.prezel.core.model.presentation.ScriptErrorType
import com.team.prezel.core.model.presentation.SentenceAnalysisDetail
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.model.presentation.WordAnalysisDetail
import com.team.prezel.core.model.presentation.WordAnalysisStatus
import com.team.prezel.core.network.model.presentation.GetMainDataResponse
import com.team.prezel.core.network.model.presentation.GetPracticeRecordsResponse
import com.team.prezel.core.network.model.presentation.GetPresentationsResponse
import com.team.prezel.core.network.model.presentation.PresentationExpectedQuestionResponse
import com.team.prezel.core.network.model.presentation.PresentationGrowthResponse
import com.team.prezel.core.network.model.presentation.PresentationScriptAnalysisResponse
import com.team.prezel.core.network.model.presentation.PresentationScriptDetailResponse
import com.team.prezel.core.network.model.presentation.PresentationSentenceAnalysisResponse
import com.team.prezel.core.network.model.presentation.PresentationSummaryResponse
import com.team.prezel.core.network.model.presentation.PresentationWordAnalysisResponse
import com.team.prezel.core.network.model.presentation.PresentationWordDetailResponse
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate

internal fun PresentationSummaryResponse.toDomain(): PresentationAnalysisSummary =
    PresentationAnalysisSummary(
        presentationId = presentationId,
        analysisResultId = analysisResultId,
        title = name,
        category = Category.from(value = type),
        purpose = Purpose.from(value = purpose),
        style = Style.from(value = style),
        audience = Audience.from(value = audience),
        analyzedAt = analysisDate,
        durationSeconds = durationSeconds,
        formattedDuration = formattedDuration,
        spm = spm,
        speedEvaluation = RecordingSpeed.from(value = speedEval),
        summaryFeedback = summaryFeedback,
        accuracyScore = accuracyScore,
        scriptMatchRate = scriptMatchRate,
        spellErrorCount = spellErrorCount,
        grammarErrorCount = grammarErrorCount,
        totalErrorCount = totalErrorCount,
        growth = growthGraph?.map { item -> item.toDomain() }.orEmpty(),
        expectedQuestions = expectedQuestions.map { item -> item.toDomain() },
        selfFeedback = reviewContent,
    )

private fun PresentationGrowthResponse.toDomain(): PresentationGrowthPoint =
    PresentationGrowthPoint(
        attempt = attempt,
        accuracyScore = accuracyScore,
        scriptMatchRate = scriptMatchRate,
    )

private fun PresentationExpectedQuestionResponse.toDomain(): ExpectedQuestion =
    ExpectedQuestion(
        question = question,
        answer = answer,
    )

internal fun PresentationScriptDetailResponse.toDomain(): PresentationScriptDetail =
    PresentationScriptDetail(
        originalScript = originalScript,
        scriptCorrections = scriptDetails.map { item -> item.toDomain() },
    )

internal fun PresentationScriptAnalysisResponse.toDomain(): ScriptCorrection =
    ScriptCorrection(
        errorType = ScriptErrorType.from(value = errorType),
        sentence = sentence,
        originalText = originalText,
        correctedText = correctedText,
        reason = reason,
        startIndex = startIndex,
        endIndex = endIndex,
    )

internal fun PresentationWordDetailResponse.toDomain(): PresentationWordDetail =
    PresentationWordDetail(
        presentationId = presentationId,
        audioUrl = audioUrl,
        sentenceDetails = sentenceDetails.map { sentence -> sentence.toDomain() }.toImmutableList(),
    )

internal fun PresentationSentenceAnalysisResponse.toDomain(): SentenceAnalysisDetail =
    SentenceAnalysisDetail(
        sentence = sentence,
        status = WordAnalysisStatus.from(value = status),
        mainFeedback = mainFeedback,
        subFeedback = subFeedback,
        accuracy = accuracy,
        startTimeMs = startTimeMs,
        endTimeMs = endTimeMs,
        wordDetails = wordDetails.map { word -> word.toDomain() }.toImmutableList(),
    )

internal fun PresentationWordAnalysisResponse.toDomain(): WordAnalysisDetail =
    WordAnalysisDetail(
        word = word,
        status = WordAnalysisStatus.from(value = status),
        accuracy = accuracy,
        startTimeMs = startTimeMs,
        endTimeMs = endTimeMs,
    )

internal fun GetPresentationsResponse.toDomain(): PresentationInfo =
    PresentationInfo(
        id = presentationId,
        title = title,
        presentationDate = LocalDate.parse(presentationDate),
        category = Category.from(value = type),
        purpose = Purpose.from(value = purpose),
        style = Style.from(value = style),
        audience = Audience.from(value = audience),
        dDay = dday,
    )

internal fun GetPracticeRecordsResponse.toDomain(): PracticeRecords =
    PracticeRecords(
        dates = dates.map(LocalDate::parse),
        startDate = LocalDate.parse(startDate),
        endDate = LocalDate.parse(endDate),
    )

internal fun GetMainDataResponse.toDomain(): MainData =
    MainData(
        presentationId = presentationId.toLong(),
        title = title,
        type = type,
        presentationDate = LocalDate.parse(presentationDate),
        isPast = isPast,
        dDay = dDay,
        growthGraph = growthGraph?.map { item -> item.toDomain() }.orEmpty(),
    )

private fun GetMainDataResponse.GrowthGraph.toDomain(): PresentationGrowthPoint =
    PresentationGrowthPoint(
        attempt = attempt,
        accuracyScore = accuracyScore,
        scriptMatchRate = scriptMatchRate,
    )
