package com.team.prezel.core.data.repository

import com.team.prezel.core.data.error.mapDomainFailure
import com.team.prezel.core.data.mapper.toDomain
import com.team.prezel.core.domain.repository.practice.PracticeRepository
import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import com.team.prezel.core.model.practice.PracticeScript
import com.team.prezel.core.network.datasource.PracticeRemoteDataSource
import javax.inject.Inject

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
}
