package com.team.prezel.core.domain.usecase.practice

import com.team.prezel.core.domain.repository.practice.PracticeRepository
import com.team.prezel.core.model.practice.PracticeRecordingUpload
import javax.inject.Inject

class UploadPracticeRecordingUseCase @Inject constructor(
    private val practiceRepository: PracticeRepository,
) {
    suspend operator fun invoke(recordingFilePath: String): Result<PracticeRecordingUpload> =
        practiceRepository.uploadPracticeRecording(recordingFilePath = recordingFilePath)
}
