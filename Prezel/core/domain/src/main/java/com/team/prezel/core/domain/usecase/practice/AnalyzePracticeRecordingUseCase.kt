package com.team.prezel.core.domain.usecase.practice

import com.team.prezel.core.domain.repository.practice.PracticeRepository
import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import javax.inject.Inject

/**
 * 연습 녹음본을 업로드하고 분석 결과를 조회하는 UseCase.
 */
class AnalyzePracticeRecordingUseCase @Inject constructor(
    private val practiceRepository: PracticeRepository,
) {
    suspend operator fun invoke(recordingFilePath: String): Result<PracticeRecordingAnalysisResult> =
        practiceRepository
            .uploadPracticeRecording(recordingFilePath = recordingFilePath)
            .mapCatching { upload ->
                practiceRepository.fetchPracticeRecordingAnalysisResult(recordingId = upload.id).getOrThrow()
            }
}
