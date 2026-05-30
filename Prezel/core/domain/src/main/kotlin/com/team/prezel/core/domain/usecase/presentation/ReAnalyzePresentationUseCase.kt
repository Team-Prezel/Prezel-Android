package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import javax.inject.Inject

class ReAnalyzePresentationUseCase @Inject constructor(
    private val presentationRepository: PresentationRepository,
) {
    suspend operator fun invoke(
        presentationId: Long,
        script: String?,
        scriptFilePath: String?,
        audioFilePath: String,
    ): Result<PresentationAnalysisSummary> =
        presentationRepository.reAnalyzePresentation(
            presentationId = presentationId,
            script = script,
            scriptFilePath = scriptFilePath,
            audioFilePath = audioFilePath,
        )
}
