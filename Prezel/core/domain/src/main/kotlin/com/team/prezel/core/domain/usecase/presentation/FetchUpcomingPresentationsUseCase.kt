package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.PresentationInfo
import javax.inject.Inject

class FetchUpcomingPresentationsUseCase @Inject constructor(
    private val repository: PresentationRepository,
) {
    suspend operator fun invoke(): Result<List<PresentationInfo>> = repository.getUpcomingPresentations()
}
