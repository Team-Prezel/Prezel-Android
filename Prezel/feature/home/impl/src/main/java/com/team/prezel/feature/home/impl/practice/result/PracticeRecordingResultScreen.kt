package com.team.prezel.feature.home.impl.practice.result

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        PracticeRecordingAnalysisStatus.Success -> PracticeRecordingResultPage(
            pronunciationScore = 90,
            speed = PracticeAnalysisSpeed.ADEQUATE,
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
