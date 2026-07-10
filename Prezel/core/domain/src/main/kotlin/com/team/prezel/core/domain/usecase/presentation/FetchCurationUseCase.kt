package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.Curation
import javax.inject.Inject

class FetchCurationUseCase @Inject constructor(
    private val presentationRepository: PresentationRepository,
) {
    suspend operator fun invoke(presentationId: Long): Result<List<Curation>> = presentationRepository.getCuration(presentationId = presentationId)
}
