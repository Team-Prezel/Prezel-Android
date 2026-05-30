package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.PracticeRecords
import javax.inject.Inject

class FetchPracticeRecordsUseCase @Inject constructor(
    private val repository: PresentationRepository,
) {
    suspend operator fun invoke(presentationId: Long): Result<PracticeRecords> = repository.getPracticeRecords(presentationId = presentationId)
}
