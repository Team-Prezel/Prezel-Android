package com.team.prezel.feature.practice.impl.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult

@Immutable
internal sealed interface PracticeRecordingAnalysisStatus {
    data object Ready : PracticeRecordingAnalysisStatus

    data object Loading : PracticeRecordingAnalysisStatus

    data class Success(
        val result: PracticeRecordingAnalysisResult,
    ) : PracticeRecordingAnalysisStatus

    data class Error(
        val type: PracticeRecordingAnalysisErrorType,
    ) : PracticeRecordingAnalysisStatus
}

internal enum class PracticeRecordingAnalysisErrorType {
    VOICE_RECOGNITION_FAILED,
    ANALYSIS_FAILED,
}
