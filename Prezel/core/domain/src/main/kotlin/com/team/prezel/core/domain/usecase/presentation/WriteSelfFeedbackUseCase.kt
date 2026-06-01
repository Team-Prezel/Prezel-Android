package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import javax.inject.Inject

class WriteSelfFeedbackUseCase @Inject constructor(
    private val presentationRepository: PresentationRepository,
) {
    suspend operator fun invoke(
        presentationId: Long,
        content: String,
    ): Result<Unit> =
        presentationRepository.writeSelfFeedback(
            presentationId = presentationId,
            content = content,
        )
}
