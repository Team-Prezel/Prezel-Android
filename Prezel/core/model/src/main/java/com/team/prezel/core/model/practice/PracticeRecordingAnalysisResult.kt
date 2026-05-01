package com.team.prezel.core.model.practice

data class PracticeRecordingAnalysisResult(
    val pronunciationScore: Int,
    val speed: PracticeRecordingSpeed,
)

enum class PracticeRecordingSpeed {
    SLOW,
    ADEQUATE,
    FAST,
}
