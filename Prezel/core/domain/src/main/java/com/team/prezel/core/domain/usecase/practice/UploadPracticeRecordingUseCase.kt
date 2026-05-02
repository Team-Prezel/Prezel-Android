package com.team.prezel.core.domain.usecase.practice

import com.team.prezel.core.domain.repository.practice.PracticeRepository
import com.team.prezel.core.model.practice.PracticeRecordingUpload
import javax.inject.Inject

/**
 * 연습 녹음본 파일을 업로드하는 UseCase.
 *
 * ### 동작 흐름
 * 1. 호출부로부터 전달받은 녹음본 파일 경로를 입력값으로 받습니다.
 * 2. [com.team.prezel.core.domain.repository.practice.PracticeRepository.uploadPracticeRecording]을 호출하여 녹음본 업로드를 요청합니다.
 * 3. 업로드 결과에 따라 녹음본 업로드 정보 또는 예외를 포함한 [Result]를 반환합니다.
 */
class UploadPracticeRecordingUseCase @Inject constructor(
    private val practiceRepository: PracticeRepository,
) {
    suspend operator fun invoke(recordingFilePath: String): Result<PracticeRecordingUpload> =
        practiceRepository.uploadPracticeRecording(recordingFilePath = recordingFilePath)
}
