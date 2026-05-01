package com.team.prezel.feature.home.impl.practice.analysis

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.feature.home.impl.practice.analysis.component.PracticeAnalysisSpeed
import com.team.prezel.feature.home.impl.practice.analysis.component.PracticeRecordingAnalysisErrorPage
import com.team.prezel.feature.home.impl.practice.analysis.component.PracticeRecordingAnalysisLoadingPage
import com.team.prezel.feature.home.impl.practice.analysis.component.PracticeRecordingAnalysisSuccessPage
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingAnalysisStatus

@Composable
internal fun PracticeRecordingAnalysisScreen(
    analysisStatus: PracticeRecordingAnalysisStatus,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (analysisStatus) {
        PracticeRecordingAnalysisStatus.Loading -> PracticeRecordingAnalysisLoadingPage(modifier = modifier)
        PracticeRecordingAnalysisStatus.Success -> PracticeRecordingAnalysisSuccessPage(
            pronunciationScore = 90,
            speed = PracticeAnalysisSpeed.ADEQUATE,
            onBack = onBack,
            onComplete = onComplete,
            modifier = modifier,
        )

        is PracticeRecordingAnalysisStatus.Error -> PracticeRecordingAnalysisErrorPage(
            errorType = analysisStatus.type,
            onRetry = onRetry,
            modifier = modifier,
        )

        PracticeRecordingAnalysisStatus.Ready -> Unit
    }
}
