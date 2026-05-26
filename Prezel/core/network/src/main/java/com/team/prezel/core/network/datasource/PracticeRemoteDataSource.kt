package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.practice.AnalyzePracticeRecordingResponse
import com.team.prezel.core.network.model.practice.PracticeSentenceResponse

interface PracticeRemoteDataSource {
    suspend fun getPracticeSentence(): PracticeSentenceResponse

    suspend fun analyzePracticeRecording(
        recordingFilePath: String,
        referenceText: String,
    ): AnalyzePracticeRecordingResponse
}
