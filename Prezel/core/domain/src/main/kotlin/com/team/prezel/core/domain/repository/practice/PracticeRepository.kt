package com.team.prezel.core.domain.repository.practice

import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import com.team.prezel.core.model.practice.PracticeScript
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.PresentationRecordingAnalysisResult
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style

interface PracticeRepository {
    suspend fun fetchPracticeScript(): Result<PracticeScript>

    suspend fun analyzePracticeRecording(
        recordingFilePath: String,
        referenceText: String,
    ): Result<PracticeRecordingAnalysisResult>

    suspend fun analyzePresentationRecording(
        name: String,
        date: String,
        category: Category,
        purpose: Purpose,
        style: Style,
        audience: Audience,
        script: String?,
        scriptFilePath: String?,
        audioFilePath: String,
    ): Result<PresentationRecordingAnalysisResult>
}
