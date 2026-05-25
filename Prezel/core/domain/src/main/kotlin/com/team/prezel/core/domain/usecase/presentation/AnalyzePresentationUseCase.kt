package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.PresentationAnalysisSummary
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import javax.inject.Inject

class AnalyzePresentationUseCase @Inject constructor(
    private val presentationRepository: PresentationRepository,
) {
    suspend operator fun invoke(
        name: String,
        date: String,
        category: Category,
        purpose: Purpose,
        style: Style,
        audience: Audience,
        script: String?,
        scriptFilePath: String?,
        audioFilePath: String,
    ): Result<PresentationAnalysisSummary> =
        presentationRepository.analyzePresentation(
            name = name,
            date = date,
            category = category,
            purpose = purpose,
            style = style,
            audience = audience,
            script = script,
            scriptFilePath = scriptFilePath,
            audioFilePath = audioFilePath,
        )
}
