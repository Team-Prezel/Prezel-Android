package com.team.prezel.core.data.repository

import com.team.prezel.core.data.error.mapDomainFailure
import com.team.prezel.core.data.mapper.toDomain
import com.team.prezel.core.data.mapper.toRequestAudience
import com.team.prezel.core.data.mapper.toRequestPurpose
import com.team.prezel.core.data.mapper.toRequestStyle
import com.team.prezel.core.data.mapper.toRequestType
import com.team.prezel.core.domain.repository.practice.PracticeRepository
import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import com.team.prezel.core.model.practice.PracticeScript
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.PresentationRecordingAnalysisResult
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
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
}
