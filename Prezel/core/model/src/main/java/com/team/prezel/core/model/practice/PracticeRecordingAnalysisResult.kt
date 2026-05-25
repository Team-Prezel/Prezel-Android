package com.team.prezel.core.model.practice

data class PracticeRecordingAnalysisResult(
    val pronunciationScore: Int,
    val speed: RecordingSpeed,
    val overallEvaluation: PracticeRecordingOverallEvaluation,
)

enum class PracticeRecordingOverallEvaluation(
    val value: String,
) {
    PERFECT("PERFECT"),
    GOOD("GOOD"),
    TRY("TRY"),
    ;

    companion object {
        fun from(value: String): PracticeRecordingOverallEvaluation =
            entries.find { entry ->
                entry.value == value.uppercase()
            } ?: throw IllegalArgumentException("지원하지 않는 타입입니다.")
    }
}
