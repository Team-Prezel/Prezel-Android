package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import javax.inject.Inject

class FetchPresentationDetailUseCase @Inject constructor(
    private val presentationRepository: PresentationRepository,
) {
    suspend operator fun invoke(
        presentationId: Long,
        isPast: Boolean = false,
    ): Result<PresentationAnalysisSummary> {
        if (isPast) return presentationRepository.getPastPresentationDetail(presentationId = presentationId)

        return presentationRepository.getUpcomingPresentationDetail(presentationId = presentationId)
    }
}
