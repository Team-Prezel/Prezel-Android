package com.team.prezel.feature.practice.impl.analysis

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.feature.practice.impl.analysis.component.PracticeRecordingAnalysisFailurePage
import com.team.prezel.feature.practice.impl.analysis.component.PracticeRecordingAnalysisLoadingPage
import com.team.prezel.feature.practice.impl.analysis.component.PracticeRecordingResultPage
import com.team.prezel.feature.practice.impl.analysis.contract.PracticeRecordingAnalysisUiIntent
import com.team.prezel.feature.practice.impl.analysis.contract.PracticeRecordingAnalysisUiState

@Composable
internal fun PracticeRecordingAnalysisScreen(
    recordingFilePath: String,
    referenceText: String,
    onRetry: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PracticeRecordingAnalysisViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(recordingFilePath, referenceText) {
        viewModel.onIntent(
            PracticeRecordingAnalysisUiIntent.Analyze(
                recordingFilePath = recordingFilePath,
                referenceText = referenceText,
            ),
        )
    }

    PracticeRecordingAnalysisScreen(
        uiState = uiState,
        onRetry = onRetry,
        onComplete = onComplete,
        modifier = modifier,
    )
}

@Composable
private fun PracticeRecordingAnalysisScreen(
    uiState: PracticeRecordingAnalysisUiState,
    onRetry: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is PracticeRecordingAnalysisUiState.Loading -> PracticeRecordingAnalysisLoadingPage(modifier = modifier)
        is PracticeRecordingAnalysisUiState.Success -> PracticeRecordingResultPage(
            pronunciationScore = uiState.result.pronunciationScore,
            speed = uiState.result.speed,
            overallEvaluation = uiState.result.overallEvaluation,
            onComplete = onComplete,
            modifier = modifier,
        )

        is PracticeRecordingAnalysisUiState.Error -> PracticeRecordingAnalysisFailurePage(
            errorType = uiState.type,
            onRetry = onRetry,
            modifier = modifier,
        )
    }
}
