package com.team.prezel.core.domain.usecase.practice

import com.team.prezel.core.domain.repository.practice.PracticeRepository
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.PresentationRecordingAnalysisResult
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import javax.inject.Inject

class AnalyzePresentationRecordingUseCase @Inject constructor(
    private val practiceRepository: PracticeRepository,
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
    ): Result<PresentationRecordingAnalysisResult> =
        practiceRepository.analyzePresentationRecording(
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
