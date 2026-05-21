package com.team.prezel.core.domain.repository.presentation

import com.team.prezel.core.model.presentation.PresentationAnalysisRequest
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import com.team.prezel.core.model.presentation.PresentationScriptDetail
import com.team.prezel.core.model.presentation.PresentationWordDetail

interface PresentationRepository {
    suspend fun analyzePresentation(request: PresentationAnalysisRequest): Result<PresentationAnalysisSummary>

    suspend fun reAnalyzePresentation(
        presentationId: Long,
        audioFilePath: String,
    ): Result<PresentationAnalysisSummary>

    suspend fun fetchScriptDetail(analysisResultId: Long): Result<PresentationScriptDetail>

    suspend fun fetchWordDetail(analysisResultId: Long): Result<PresentationWordDetail>

    suspend fun deleteAnalysis(analysisResultId: Long): Result<Unit>
}
