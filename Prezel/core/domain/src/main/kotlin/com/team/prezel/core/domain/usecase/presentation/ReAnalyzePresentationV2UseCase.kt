package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import javax.inject.Inject

class ReAnalyzePresentationV2UseCase @Inject constructor(
    private val presentationRepository: PresentationRepository,
) {
    suspend operator fun invoke(
        presentationId: Long,
        script: String?,
        scriptFilePath: String?,
        audioFilePath: String?,
    ): Result<Unit> =
        presentationRepository.reAnalyzePresentationV2(
            presentationId = presentationId,
            script = script,
            scriptFilePath = scriptFilePath,
            audioFilePath = audioFilePath,
        )
}
