package com.team.prezel.feature.practice.impl.analysis.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.practice.impl.model.PracticeRecordingAnalysisErrorType

@Immutable
internal sealed interface PracticeRecordingAnalysisUiState : UiState {
    @Immutable
    data object Loading : PracticeRecordingAnalysisUiState

    @Immutable
    data class Success(
        val result: PracticeRecordingAnalysisResult,
    ) : PracticeRecordingAnalysisUiState

    @Immutable
    data class Error(
        val type: PracticeRecordingAnalysisErrorType,
    ) : PracticeRecordingAnalysisUiState
}
