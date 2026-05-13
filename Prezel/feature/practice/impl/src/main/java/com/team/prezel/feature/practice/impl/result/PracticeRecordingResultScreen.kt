package com.team.prezel.feature.practice.impl.result

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.feature.practice.impl.contract.PracticeRecordingUiState
import com.team.prezel.feature.practice.impl.result.component.PracticeRecordingAnalysisFailurePage
import com.team.prezel.feature.practice.impl.result.component.PracticeRecordingAnalysisLoadingPage
import com.team.prezel.feature.practice.impl.result.component.PracticeRecordingResultPage

@Composable
internal fun PracticeRecordingResultScreen(
    uiState: PracticeRecordingUiState.Analysis,
    onRetry: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is PracticeRecordingUiState.Analysis.Loading -> PracticeRecordingAnalysisLoadingPage(modifier = modifier)
        is PracticeRecordingUiState.Analysis.Success -> PracticeRecordingResultPage(
            pronunciationScore = uiState.result.pronunciationScore,
            speed = uiState.result.speed,
            overallEvaluation = uiState.result.overallEvaluation,
            onComplete = onComplete,
            modifier = modifier,
        )

        is PracticeRecordingUiState.Analysis.Error -> PracticeRecordingAnalysisFailurePage(
            errorType = uiState.type,
            onRetry = onRetry,
            modifier = modifier,
        )
    }
}
