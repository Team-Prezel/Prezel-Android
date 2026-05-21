package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.PresentationAnalysisRequest
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import javax.inject.Inject

class AnalyzePresentationUseCase @Inject constructor(
    private val presentationRepository: PresentationRepository,
) {
    suspend operator fun invoke(request: PresentationAnalysisRequest): Result<PresentationAnalysisSummary> =
        presentationRepository.analyzePresentation(request = request)
}
