package com.team.prezel.core.domain.usecase.practice

import com.team.prezel.core.domain.repository.practice.PracticeRepository
import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import javax.inject.Inject

/**
 * 업로드된 연습 녹음본의 분석 결과를 조회하는 UseCase.
 *
 * ### 동작 흐름
 * 1. 호출부로부터 전달받은 녹음본 ID를 입력값으로 받습니다.
 * 2. [com.team.prezel.core.domain.repository.practice.PracticeRepository.fetchPracticeRecordingAnalysisResult]를 호출하여 분석 결과 조회를 요청합니다.
 * 3. 조회 결과에 따라 분석 결과 또는 예외를 포함한 [Result]를 반환합니다.
 */
class FetchPracticeRecordingAnalysisResultUseCase @Inject constructor(
    private val practiceRepository: PracticeRepository,
) {
    suspend operator fun invoke(recordingId: Long): Result<PracticeRecordingAnalysisResult> =
        practiceRepository.fetchPracticeRecordingAnalysisResult(recordingId = recordingId)
}
