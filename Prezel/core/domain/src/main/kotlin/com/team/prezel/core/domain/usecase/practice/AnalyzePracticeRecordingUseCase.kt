package com.team.prezel.core.domain.usecase.practice

import com.team.prezel.core.domain.repository.practice.PracticeRepository
import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import javax.inject.Inject

class AnalyzePracticeRecordingUseCase @Inject constructor(
    private val practiceRepository: PracticeRepository,
) {
    suspend operator fun invoke(
        recordingFilePath: String,
        referenceText: String,
    ): Result<PracticeRecordingAnalysisResult> =
        practiceRepository.analyzePracticeRecording(
            recordingFilePath = recordingFilePath,
            referenceText = referenceText,
        )
}
