package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import javax.inject.Inject

class DeletePresentationAnalysisUseCase @Inject constructor(
    private val presentationRepository: PresentationRepository,
) {
    suspend operator fun invoke(analysisResultId: Long): Result<Unit> =
        presentationRepository.deleteAnalysis(analysisResultId = analysisResultId)
}
