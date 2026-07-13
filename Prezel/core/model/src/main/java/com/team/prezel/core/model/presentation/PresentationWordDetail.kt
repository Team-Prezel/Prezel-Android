package com.team.prezel.core.model.presentation

import kotlinx.collections.immutable.ImmutableList

data class PresentationWordDetail(
    val presentationId: Long,
    val audioUrl: String,
    val sentenceDetails: ImmutableList<SentenceAnalysisDetail>,
)

data class SentenceAnalysisDetail(
    val sentence: String,
    val status: WordAnalysisStatus,
    val mainFeedback: String,
    val subFeedback: String,
    val guideScript: String = "",
    val accuracy: Double,
    val startTimeMs: Long,
    val endTimeMs: Long,
    val wordDetails: ImmutableList<WordAnalysisDetail>,
)

data class WordAnalysisDetail(
    val word: String,
    val status: WordAnalysisStatus,
    val accuracy: Double,
    val startTimeMs: Long,
    val endTimeMs: Long,
)

enum class WordAnalysisStatus(
    val value: String,
) {
    EXCELLENT("Excellent"),
    GOOD("Good"),
    INSERTION("Insertion"),
    STUTTER("Stutter"),
    MISPRONUNCIATION("Mispronunciation"),
    OMISSION("Omission"),
    UNKNOWN("Unknown"),
    ;

    companion object {
        fun from(value: String): WordAnalysisStatus =
            entries.firstOrNull { status -> status.value.equals(value.trim(), ignoreCase = true) } ?: UNKNOWN
    }
}
