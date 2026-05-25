package com.team.prezel.core.network.datasource

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
        audioFilePath: String,
    ): PresentationSummaryResponse

    suspend fun getScriptDetail(analysisResultId: Long): PresentationScriptDetailResponse

    suspend fun getWordDetail(analysisResultId: Long): PresentationWordDetailResponse

    suspend fun deleteAnalysis(analysisResultId: Long)
}
