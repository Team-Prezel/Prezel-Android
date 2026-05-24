package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.practice.AnalyzePracticeRecordingResponse
import com.team.prezel.core.network.model.practice.PracticeSentenceResponse
import com.team.prezel.core.network.model.practice.PresentationRecordingAnalysisResponse

interface PracticeRemoteDataSource {
    suspend fun getPracticeSentence(): PracticeSentenceResponse

    suspend fun analyzePracticeRecording(
        recordingFilePath: String,
        referenceText: String,
    ): AnalyzePracticeRecordingResponse

    suspend fun analyzePresentationRecording(
        name: String,
        date: String,
        type: String,
        purpose: String,
        style: String,
        audience: String,
        script: String?,
        scriptFilePath: String?,
        audioFilePath: String,
    ): PresentationRecordingAnalysisResponse
}
