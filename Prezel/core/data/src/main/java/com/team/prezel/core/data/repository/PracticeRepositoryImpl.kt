package com.team.prezel.core.data.repository

import com.team.prezel.core.data.error.mapDomainFailure
import com.team.prezel.core.domain.repository.practice.PracticeRepository
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
import com.team.prezel.core.network.datasource.PracticeRemoteDataSource
import com.team.prezel.core.network.model.practice.AnalyzePracticeRecordingResponse
import com.team.prezel.core.network.model.practice.PracticeSentenceResponse
import com.team.prezel.core.network.model.practice.PresentationAnalysisAudience
import com.team.prezel.core.network.model.practice.PresentationAnalysisPurpose
import com.team.prezel.core.network.model.practice.PresentationAnalysisStyle
import com.team.prezel.core.network.model.practice.PresentationAnalysisType
import com.team.prezel.core.network.model.practice.PresentationRecordingAnalysisResponse
import javax.inject.Inject
import kotlin.math.roundToInt

internal class PracticeRepositoryImpl @Inject constructor(
    private val practiceRemoteDataSource: PracticeRemoteDataSource,
) : PracticeRepository {
    override suspend fun fetchPracticeScript(): Result<PracticeScript> =
        runCatching {
            practiceRemoteDataSource.getPracticeSentence()
        }.mapCatching { response ->
            response.toDomain()
        }.mapDomainFailure()

    override suspend fun analyzePresentationRecording(
        name: String,
        date: String,
        category: Category,
        purpose: Purpose,
        style: Style,
        audience: Audience,
        script: String?,
        scriptFilePath: String?,
        audioFilePath: String,
    ): Result<PresentationRecordingAnalysisResult> =
        runCatching {
            practiceRemoteDataSource.analyzePresentationRecording(
                name = name,
                date = date,
                type = category.toRequestType(),
                purpose = purpose.toRequestPurpose(),
                style = style.toRequestStyle(),
                audience = audience.toRequestAudience(),
                script = script,
                scriptFilePath = scriptFilePath,
                audioFilePath = audioFilePath,
            )
        }.mapCatching { response ->
            response.toDomain()
        }.mapDomainFailure()

    override suspend fun analyzePracticeRecording(
        recordingFilePath: String,
        referenceText: String,
    ): Result<PracticeRecordingAnalysisResult> =
        runCatching {
            practiceRemoteDataSource.analyzePracticeRecording(
                recordingFilePath = recordingFilePath,
                referenceText = referenceText,
            )
        }.mapCatching { response ->
            response.toDomain()
        }.mapDomainFailure()

    private fun PracticeSentenceResponse.toDomain(): PracticeScript =
        PracticeScript(
            id = PRACTICE_SCRIPT_ID,
            content = sentence,
        )

    private fun AnalyzePracticeRecordingResponse.toDomain(): PracticeRecordingAnalysisResult =
        PracticeRecordingAnalysisResult(
            pronunciationScore = accuracyScore.roundToInt(),
            speed = speedEvaluation.toPracticeRecordingSpeed(),
            overallEvaluation = overallEvaluation.toPracticeRecordingOverallEvaluation(),
        )

    private fun PresentationRecordingAnalysisResponse.toDomain(): PresentationRecordingAnalysisResult =
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

    private fun PresentationRecordingAnalysisResponse.GrowthGraph.toDomain(): PresentationGrowthGraph =
        PresentationGrowthGraph(
            attempt = attempt,
            accuracyScore = accuracyScore,
            scriptMatchRate = scriptMatchRate,
        )

    private fun PresentationRecordingAnalysisResponse.ExpectedQuestion.toDomain(): PresentationExpectedQuestion =
        PresentationExpectedQuestion(
            question = question,
            answer = answer,
        )

    private fun Category.toRequestType(): PresentationAnalysisType =
        when (this) {
            Category.EDUCATION -> PresentationAnalysisType.EDUCATION
            Category.REPORT -> PresentationAnalysisType.WORK
            Category.PERSUASION -> PresentationAnalysisType.OFFER
            Category.EVENT -> PresentationAnalysisType.EVENT
        }

    private fun Purpose.toRequestPurpose(): PresentationAnalysisPurpose =
        when (this) {
            Purpose.CONTENT_DELIVERY -> PresentationAnalysisPurpose.INFO
            Purpose.IMPROVE_UNDERSTANDING -> PresentationAnalysisPurpose.UNDERSTANDING
            Purpose.BUILD_EMPATHY -> PresentationAnalysisPurpose.EMPATHY
        }

    private fun Style.toRequestStyle(): PresentationAnalysisStyle =
        when (this) {
            Style.PROFESSIONAL -> PresentationAnalysisStyle.FORMAL
            Style.FRIENDLY -> PresentationAnalysisStyle.FRIENDLY
            Style.CALM -> PresentationAnalysisStyle.CALM
            Style.COMFORTABLE -> PresentationAnalysisStyle.CASUAL
        }

    private fun Audience.toRequestAudience(): PresentationAnalysisAudience =
        when (this) {
            Audience.GENERAL_AUDIENCE -> PresentationAnalysisAudience.GENERAL
            Audience.EXPERT -> PresentationAnalysisAudience.PROFESSIONAL
            Audience.TEAMMATES -> PresentationAnalysisAudience.TEAMMATE
        }

    private fun String.toPracticeRecordingSpeed(): PracticeRecordingSpeed =
        when {
            contains("느려요") -> PracticeRecordingSpeed.SLOW
            contains("빨라요") -> PracticeRecordingSpeed.FAST
            else -> PracticeRecordingSpeed.ADEQUATE
        }

    private fun String.toPracticeRecordingOverallEvaluation(): PracticeRecordingOverallEvaluation =
        when (this) {
            "Perfect" -> PracticeRecordingOverallEvaluation.PERFECT
            "Good" -> PracticeRecordingOverallEvaluation.GOOD
            "Try" -> PracticeRecordingOverallEvaluation.TRY
            else -> PracticeRecordingOverallEvaluation.TRY
        }

    private companion object {
        const val PRACTICE_SCRIPT_ID = 0L
    }
}
