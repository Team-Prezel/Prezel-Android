package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import javax.inject.Inject

class FetchPresentationDetailUseCase @Inject constructor(
    private val presentationRepository: PresentationRepository,
) {
    suspend operator fun invoke(presentationId: Long): Result<PresentationAnalysisSummary> =
        presentationRepository.getUpcomingPresentationDetail(presentationId = presentationId)
}
