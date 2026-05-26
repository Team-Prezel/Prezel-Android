package com.team.prezel.feature.practice.impl.analysis.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.practice.PracticeRecordingOverallEvaluation
import com.team.prezel.core.model.practice.RecordingSpeed
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.practice.impl.analysis.model.PracticeAnalysisErrorType

@Immutable
internal sealed interface PracticeAnalysisUiState : UiState {
    @Immutable
    data object Loading : PracticeAnalysisUiState

    @Immutable
    data class Success(
        val pronunciationScore: Int,
        val speed: RecordingSpeed,
        val overallEvaluation: PracticeRecordingOverallEvaluation,
    ) : PracticeAnalysisUiState

    @Immutable
    data class Error(
        val type: PracticeAnalysisErrorType,
    ) : PracticeAnalysisUiState
}
