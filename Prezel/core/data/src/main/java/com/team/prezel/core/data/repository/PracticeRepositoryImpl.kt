package com.team.prezel.core.data.repository

import com.team.prezel.core.data.error.mapDomainFailure
import com.team.prezel.core.domain.repository.practice.PracticeRepository
import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import com.team.prezel.core.model.practice.PracticeRecordingSpeed
import com.team.prezel.core.model.practice.PracticeScript
import com.team.prezel.core.network.datasource.PracticeRemoteDataSource
import com.team.prezel.core.network.model.practice.AnalyzePracticeRecordingResponse
import com.team.prezel.core.network.model.practice.PracticeSentenceResponse
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
        )

    private fun String.toPracticeRecordingSpeed(): PracticeRecordingSpeed =
        when {
            contains("느려요") -> PracticeRecordingSpeed.SLOW
            contains("빨라요") -> PracticeRecordingSpeed.FAST
            else -> PracticeRecordingSpeed.ADEQUATE
        }

    private companion object {
        const val PRACTICE_SCRIPT_ID = 0L
    }
}
