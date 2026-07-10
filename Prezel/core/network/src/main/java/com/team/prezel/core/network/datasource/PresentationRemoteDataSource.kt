package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.presentation.GetCurationResponse
import com.team.prezel.core.network.model.presentation.GetMainDataResponse
import com.team.prezel.core.network.model.presentation.GetPracticeRecordsResponse
import com.team.prezel.core.network.model.presentation.GetPresentationsResponse
import com.team.prezel.core.network.model.presentation.PresentationScriptDetailResponse
import com.team.prezel.core.network.model.presentation.PresentationSummaryResponse
import com.team.prezel.core.network.model.presentation.PresentationWordDetailResponse

interface PresentationRemoteDataSource {
    suspend fun analyzePresentation(
        name: String,
        date: String,
        type: String,
        purpose: String,
        style: String,
        audience: String,
        script: String?,
        scriptFilePath: String?,
        audioFilePath: String,
    ): PresentationSummaryResponse

    suspend fun reAnalyzePresentation(
        presentationId: Long,
        script: String?,
        scriptFilePath: String?,
        audioFilePath: String,
    ): PresentationSummaryResponse

    suspend fun getScriptDetail(analysisResultId: Long): PresentationScriptDetailResponse

    suspend fun getWordDetail(analysisResultId: Long): PresentationWordDetailResponse

    suspend fun deleteAnalysis(analysisResultId: Long)

    suspend fun getUpcomingPresentations(): List<GetPresentationsResponse>

    suspend fun getPastPresentations(): List<GetPresentationsResponse>

    suspend fun getUpcomingPresentationDetail(presentationId: Long): PresentationSummaryResponse

    suspend fun getPastPresentationDetail(presentationId: Long): PresentationSummaryResponse

    suspend fun getPracticeRecords(presentationId: Long): GetPracticeRecordsResponse

    suspend fun getMainData(): List<GetMainDataResponse>

    suspend fun writeSelfFeedback(
        presentationId: Long,
        content: String,
    )

    suspend fun getCuration(presentationId: Long): List<GetCurationResponse>
}
