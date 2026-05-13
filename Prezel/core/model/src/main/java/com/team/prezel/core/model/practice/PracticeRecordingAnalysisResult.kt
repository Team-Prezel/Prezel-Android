package com.team.prezel.core.model.practice

data class PracticeRecordingAnalysisResult(
    val pronunciationScore: Int,
    val speed: PracticeRecordingSpeed,
    val overallEvaluation: PracticeRecordingOverallEvaluation,
)

enum class PracticeRecordingSpeed {
    SLOW,
    ADEQUATE,
    FAST,
}

enum class PracticeRecordingOverallEvaluation {
    PERFECT,
    GOOD,
    TRY,
}
