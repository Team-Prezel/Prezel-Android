package com.team.prezel.core.data.repository

import com.team.prezel.core.data.error.mapDomainFailure
import com.team.prezel.core.data.mapper.toDomain
import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.MainData
import com.team.prezel.core.model.presentation.PracticeRecords
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import com.team.prezel.core.model.presentation.PresentationInfo
import com.team.prezel.core.model.presentation.PresentationScriptDetail
import com.team.prezel.core.model.presentation.PresentationWordDetail
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.network.datasource.PresentationRemoteDataSource
import com.team.prezel.core.network.model.presentation.GetMainDataResponse
import com.team.prezel.core.network.model.presentation.GetPresentationsResponse
import javax.inject.Inject

internal class PresentationRepositoryImpl @Inject constructor(
    private val presentationRemoteDataSource: PresentationRemoteDataSource,
) : PresentationRepository {
    override suspend fun analyzePresentation(
        name: String,
        date: String,
        category: Category,
        purpose: Purpose,
        style: Style,
        audience: Audience,
        script: String?,
        scriptFilePath: String?,
        audioFilePath: String,
    ): Result<Long> =
        runCatching {
            presentationRemoteDataSource.analyzePresentation(
                name = name,
                date = date,
                type = category.value,
                purpose = purpose.value,
                style = style.value,
                audience = audience.value,
                script = script,
                scriptFilePath = scriptFilePath,
                audioFilePath = audioFilePath,
            )
        }.mapCatching { response ->
            response.presentationId
        }.mapDomainFailure()

    override suspend fun reAnalyzePresentation(
        presentationId: Long,
        script: String?,
        scriptFilePath: String?,
        audioFilePath: String,
    ): Result<PresentationAnalysisSummary> =
        runCatching {
            presentationRemoteDataSource.reAnalyzePresentation(
                presentationId = presentationId,
                script = script,
                scriptFilePath = scriptFilePath,
                audioFilePath = audioFilePath,
            )
        }.mapCatching { response ->
            response.toDomain()
        }.mapDomainFailure()

    override suspend fun fetchScriptDetail(analysisResultId: Long): Result<PresentationScriptDetail> =
        runCatching {
            presentationRemoteDataSource.getScriptDetail(analysisResultId = analysisResultId)
        }.mapCatching { response ->
            response.toDomain()
        }.mapDomainFailure()

    override suspend fun fetchWordDetail(analysisResultId: Long): Result<PresentationWordDetail> =
        runCatching {
            presentationRemoteDataSource.getWordDetail(analysisResultId = analysisResultId)
        }.mapCatching { response ->
            response.toDomain()
        }.mapDomainFailure()

    override suspend fun deleteAnalysis(analysisResultId: Long): Result<Unit> =
        runCatching {
            presentationRemoteDataSource.deleteAnalysis(analysisResultId = analysisResultId)
        }.mapDomainFailure()

    override suspend fun getUpcomingPresentations(): Result<List<PresentationInfo>> =
        runCatching {
            presentationRemoteDataSource.getUpcomingPresentations()
        }.mapCatching { response ->
            response.map(GetPresentationsResponse::toDomain)
        }.mapDomainFailure()

    override suspend fun getPastPresentations(): Result<List<PresentationInfo>> =
        runCatching {
            presentationRemoteDataSource.getPastPresentations()
        }.mapCatching { response ->
            response.map(GetPresentationsResponse::toDomain)
        }.mapDomainFailure()

    override suspend fun getUpcomingPresentationDetail(presentationId: Long): Result<PresentationAnalysisSummary> =
        runCatching {
            presentationRemoteDataSource.getUpcomingPresentationDetail(presentationId = presentationId)
        }.mapCatching { response ->
            response.toDomain()
        }.mapDomainFailure()

    override suspend fun getPastPresentationDetail(presentationId: Long): Result<PresentationAnalysisSummary> =
        runCatching {
            presentationRemoteDataSource.getPastPresentationDetail(presentationId = presentationId)
        }.mapCatching { response ->
            response.toDomain()
        }.mapDomainFailure()

    override suspend fun getPracticeRecords(presentationId: Long): Result<PracticeRecords> =
        runCatching {
            presentationRemoteDataSource.getPracticeRecords(presentationId = presentationId)
        }.mapCatching { response ->
            response.toDomain()
        }.mapDomainFailure()

    override suspend fun getMainData(): Result<List<MainData>> =
        runCatching {
            presentationRemoteDataSource.getMainData()
        }.mapCatching { response ->
            response.map(GetMainDataResponse::toDomain)
        }.mapDomainFailure()

    override suspend fun writeSelfFeedback(
        presentationId: Long,
        content: String,
    ): Result<Unit> =
        runCatching {
            presentationRemoteDataSource.writeSelfFeedback(
                presentationId = presentationId,
                content = content,
            )
        }.mapDomainFailure()
}
