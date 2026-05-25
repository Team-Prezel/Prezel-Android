package com.team.prezel.core.domain.repository.presentation

import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import com.team.prezel.core.model.presentation.PresentationScriptDetail
import com.team.prezel.core.model.presentation.PresentationWordDetail
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style

interface PresentationRepository {
    suspend fun analyzePresentation(
        name: String,
        date: String,
        category: Category,
        purpose: Purpose,
        style: Style,
        audience: Audience,
        script: String?,
        scriptFilePath: String?,
        audioFilePath: String,
    ): Result<Long>

    suspend fun reAnalyzePresentation(
        presentationId: Long,
        audioFilePath: String,
    ): Result<PresentationAnalysisSummary>

    suspend fun fetchScriptDetail(analysisResultId: Long): Result<PresentationScriptDetail>

    suspend fun fetchWordDetail(analysisResultId: Long): Result<PresentationWordDetail>

    suspend fun deleteAnalysis(analysisResultId: Long): Result<Unit>

    suspend fun getUpcomingPresentationDetail(presentationId: Long): Result<PresentationAnalysisSummary>
}
