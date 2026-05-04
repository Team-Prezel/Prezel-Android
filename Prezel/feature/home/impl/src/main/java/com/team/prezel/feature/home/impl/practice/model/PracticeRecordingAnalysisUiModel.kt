package com.team.prezel.feature.home.impl.practice.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class PracticeRecordingAnalysisUiModel(
    val pronunciationScore: Int,
    val speed: PracticeRecordingAnalysisSpeed,
)

internal enum class PracticeRecordingAnalysisSpeed {
    SLOW,
    ADEQUATE,
    FAST,
}
