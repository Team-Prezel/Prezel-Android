package com.team.prezel.feature.practice.impl.analysis.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface PracticeRecordingAnalysisUiIntent : UiIntent {
    data class Analyze(
        val recordingFilePath: String,
        val referenceText: String,
    ) : PracticeRecordingAnalysisUiIntent
}
