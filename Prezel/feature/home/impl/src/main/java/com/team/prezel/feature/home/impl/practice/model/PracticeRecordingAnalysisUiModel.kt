package com.team.prezel.feature.home.impl.practice.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.practice.PracticeRecordingSpeed

@Immutable
internal data class PracticeRecordingAnalysisUiModel(
    val pronunciationScore: Int,
    val speed: PracticeRecordingSpeed,
)
