package com.team.prezel.feature.practice.impl.analysis

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.practice.PracticeRecordingOverallEvaluation
import com.team.prezel.core.model.practice.RecordingSpeed
import com.team.prezel.feature.practice.impl.analysis.component.PracticeAnalysisFailurePage
import com.team.prezel.feature.practice.impl.analysis.component.PracticeAnalysisLoadingPage
import com.team.prezel.feature.practice.impl.analysis.component.PracticeAnalysisResultPage
import com.team.prezel.feature.practice.impl.analysis.contract.PracticeAnalysisUiState
import com.team.prezel.feature.practice.impl.analysis.model.PracticeAnalysisErrorType

@Composable
internal fun PracticeAnalysisScreen(
    onRetry: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PracticeAnalysisViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler(enabled = true) {}

    PracticeAnalysisScreen(
        uiState = uiState,
        onRetry = onRetry,
        onComplete = onComplete,
        modifier = modifier,
    )
}

@Composable
private fun PracticeAnalysisScreen(
    uiState: PracticeAnalysisUiState,
    onRetry: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is PracticeAnalysisUiState.Loading -> PracticeAnalysisLoadingPage(modifier = modifier)
        is PracticeAnalysisUiState.Success -> PracticeAnalysisResultPage(
            pronunciationScore = uiState.pronunciationScore,
            speed = uiState.speed,
            overallEvaluation = uiState.overallEvaluation,
            onComplete = onComplete,
            modifier = modifier,
        )

        is PracticeAnalysisUiState.Error -> PracticeAnalysisFailurePage(
            errorType = uiState.type,
            onRetry = onRetry,
            modifier = modifier,
        )
    }
}

@BasicPreview
@Composable
private fun AnalysisScreenLoadingPreview() {
    PrezelTheme {
        PracticeAnalysisScreen(
            uiState = PracticeAnalysisUiState.Loading,
            onRetry = {},
            onComplete = {},
        )
    }
}

@BasicPreview
@Composable
private fun AnalysisScreenSuccessPreview() {
    PrezelTheme {
        PracticeAnalysisScreen(
            uiState = PracticeAnalysisUiState.Success(
                pronunciationScore = 85,
                speed = RecordingSpeed.ADEQUATE,
                overallEvaluation = PracticeRecordingOverallEvaluation.GOOD,
            ),
            onRetry = {},
            onComplete = {},
        )
    }
}

@BasicPreview
@Composable
private fun AnalysisScreenErrorPreview() {
    PrezelTheme {
        PracticeAnalysisScreen(
            uiState = PracticeAnalysisUiState.Error(
                type = PracticeAnalysisErrorType.ANALYSIS_FAILED,
            ),
            onRetry = {},
            onComplete = {},
        )
    }
}
