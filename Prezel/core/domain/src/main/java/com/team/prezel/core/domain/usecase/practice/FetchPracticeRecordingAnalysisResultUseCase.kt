package com.team.prezel.core.domain.usecase.practice

import com.team.prezel.core.domain.repository.practice.PracticeRepository
import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import javax.inject.Inject

class FetchPracticeRecordingAnalysisResultUseCase @Inject constructor(
    private val practiceRepository: PracticeRepository,
) {
    suspend operator fun invoke(recordingId: Long): Result<PracticeRecordingAnalysisResult> =
        practiceRepository.fetchPracticeRecordingAnalysisResult(recordingId = recordingId)
}
