package com.team.prezel.core.data.mapper

import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import com.team.prezel.core.model.practice.PracticeRecordingOverallEvaluation
import com.team.prezel.core.model.practice.PracticeRecordingSpeed
import com.team.prezel.core.model.practice.PracticeScript
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.PresentationExpectedQuestion
import com.team.prezel.core.model.presentation.PresentationGrowthGraph
import com.team.prezel.core.model.presentation.PresentationRecordingAnalysisResult
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.network.model.practice.AnalyzePracticeRecordingResponse
import com.team.prezel.core.network.model.practice.PracticeSentenceResponse
import com.team.prezel.core.network.model.practice.PresentationRecordingAnalysisResponse
import kotlin.math.roundToInt

internal fun PracticeSentenceResponse.toDomain(): PracticeScript =
    PracticeScript(
        id = PRACTICE_SCRIPT_ID,
        content = sentence,
    )

internal fun AnalyzePracticeRecordingResponse.toDomain(): PracticeRecordingAnalysisResult =
    PracticeRecordingAnalysisResult(
        pronunciationScore = accuracyScore.roundToInt(),
        speed = speedEvaluation.toPracticeRecordingSpeed(),
        overallEvaluation = overallEvaluation.toPracticeRecordingOverallEvaluation(),
    )

internal fun PresentationRecordingAnalysisResponse.toDomain(): PresentationRecordingAnalysisResult =
    PresentationRecordingAnalysisResult(
        presentationId = presentationId,
        analysisResultId = analysisResultId,
        name = name,
        type = type,
        purpose = purpose,
        style = style,
        audience = audience,
        analysisDate = analysisDate,
        durationSeconds = durationSeconds,
        formattedDuration = formattedDuration,
        spm = spm,
        speedEval = speedEval,
        summaryFeedback = summaryFeedback,
        accuracyScore = accuracyScore,
        scriptMatchRate = scriptMatchRate,
        spellErrorCount = spellErrorCount,
        grammarErrorCount = grammarErrorCount,
        totalErrorCount = totalErrorCount,
        growthGraph = growthGraph.map { it.toDomain() },
        expectedQuestions = expectedQuestions.map { it.toDomain() },
    )

internal fun Category.toRequestType(): String = name

internal fun Purpose.toRequestPurpose(): String = name

internal fun Style.toRequestStyle(): String = name

internal fun Audience.toRequestAudience(): String = name

internal fun PresentationRecordingAnalysisResponse.GrowthGraph.toDomain(): PresentationGrowthGraph =
    PresentationGrowthGraph(
        attempt = attempt,
        accuracyScore = accuracyScore,
        scriptMatchRate = scriptMatchRate,
    )

internal fun PresentationRecordingAnalysisResponse.ExpectedQuestion.toDomain(): PresentationExpectedQuestion =
    PresentationExpectedQuestion(
        question = question,
        answer = answer,
    )

internal fun String.toPracticeRecordingSpeed(): PracticeRecordingSpeed =
    when {
        contains("느려요") -> PracticeRecordingSpeed.SLOW
        contains("빨라요") -> PracticeRecordingSpeed.FAST
        else -> PracticeRecordingSpeed.ADEQUATE
    }

internal fun String.toPracticeRecordingOverallEvaluation(): PracticeRecordingOverallEvaluation =
    when (this) {
        "Perfect" -> PracticeRecordingOverallEvaluation.PERFECT
        "Good" -> PracticeRecordingOverallEvaluation.GOOD
        "Try" -> PracticeRecordingOverallEvaluation.TRY
        else -> PracticeRecordingOverallEvaluation.TRY
    }

private const val PRACTICE_SCRIPT_ID = 0L
