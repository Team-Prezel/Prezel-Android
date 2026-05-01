package com.team.prezel.core.domain.repository.practice

import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import com.team.prezel.core.model.practice.PracticeRecordingUpload
import com.team.prezel.core.model.practice.PracticeScript

interface PracticeRepository {
    suspend fun fetchPracticeScript(): Result<PracticeScript>

    suspend fun uploadPracticeRecording(recordingFilePath: String): Result<PracticeRecordingUpload>

    suspend fun fetchPracticeRecordingAnalysisResult(recordingId: Long): Result<PracticeRecordingAnalysisResult>
}
