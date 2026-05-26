package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.PresentationScriptDetail
import javax.inject.Inject

class FetchPresentationScriptDetailUseCase @Inject constructor(
    private val presentationRepository: PresentationRepository,
) {
    suspend operator fun invoke(analysisResultId: Long): Result<PresentationScriptDetail> =
        presentationRepository.fetchScriptDetail(analysisResultId = analysisResultId)
}
