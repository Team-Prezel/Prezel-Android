package com.team.prezel.core.model.presentation

data class PresentationWordDetail(
    val presentationId: Long,
    val audioUrl: String,
    val wordDetails: List<WordAnalysisDetail>,
)

data class WordAnalysisDetail(
    val word: String,
    val status: WordAnalysisStatus,
    val description: String,
    val accuracy: Double,
    val startTimeMs: Long,
    val endTimeMs: Long,
)

enum class WordAnalysisStatus(
    val value: String,
) {
    EXCELLENT("Excellent"),
    GOOD("Good"),
    STUTTER("Stutter"),
    INSERTION("Insertion"),
    OMISSION("Omission"),
    MISPRONUNCIATION("Mispronunciation"),
    UNKNOWN("Unknown"),
    ;

    companion object {
        fun from(value: String): WordAnalysisStatus = entries.firstOrNull { status -> status.value == value } ?: UNKNOWN
    }
}
