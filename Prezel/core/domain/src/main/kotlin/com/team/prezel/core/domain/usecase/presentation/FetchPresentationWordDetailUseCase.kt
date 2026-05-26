package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.PresentationWordDetail
import javax.inject.Inject

class FetchPresentationWordDetailUseCase @Inject constructor(
    private val presentationRepository: PresentationRepository,
) {
    suspend operator fun invoke(analysisResultId: Long): Result<PresentationWordDetail> =
        presentationRepository.fetchWordDetail(analysisResultId = analysisResultId)
}
