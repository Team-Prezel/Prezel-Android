package com.team.prezel.feature.home.impl.practice.result

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.model.practice.PracticeRecordingSpeed
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingAnalysisStatus
import com.team.prezel.feature.home.impl.practice.result.component.PracticeAnalysisSpeed
import com.team.prezel.feature.home.impl.practice.result.component.PracticeRecordingAnalysisFailurePage
import com.team.prezel.feature.home.impl.practice.result.component.PracticeRecordingAnalysisLoadingPage
import com.team.prezel.feature.home.impl.practice.result.component.PracticeRecordingResultPage

@Composable
internal fun PracticeRecordingResultScreen(
    analysisStatus: PracticeRecordingAnalysisStatus,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (analysisStatus) {
        PracticeRecordingAnalysisStatus.Loading -> PracticeRecordingAnalysisLoadingPage(modifier = modifier)
        is PracticeRecordingAnalysisStatus.Success -> PracticeRecordingResultPage(
            pronunciationScore = analysisStatus.result.pronunciationScore,
            speed = analysisStatus.result.speed.toUiModel(),
            onBack = onBack,
            onComplete = onComplete,
            modifier = modifier,
        )

        is PracticeRecordingAnalysisStatus.Error -> PracticeRecordingAnalysisFailurePage(
            errorType = analysisStatus.type,
            onRetry = onRetry,
            modifier = modifier,
        )

        PracticeRecordingAnalysisStatus.Ready -> Unit
    }
}

private fun PracticeRecordingSpeed.toUiModel(): PracticeAnalysisSpeed =
    when (this) {
        PracticeRecordingSpeed.SLOW -> PracticeAnalysisSpeed.SLOW
        PracticeRecordingSpeed.ADEQUATE -> PracticeAnalysisSpeed.ADEQUATE
        PracticeRecordingSpeed.FAST -> PracticeAnalysisSpeed.FAST
    }
