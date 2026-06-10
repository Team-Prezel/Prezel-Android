package com.team.prezel.feature.report.impl.accuracydetail.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.SentenceAnalysisDetail
import com.team.prezel.core.model.presentation.WordAnalysisDetail
import com.team.prezel.core.model.presentation.WordAnalysisStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
internal data class SentenceAnalysisUiModel(
    val sentence: String,
    val status: WordAnalysisStatus,
    val mainFeedback: String,
    val subFeedback: String,
    val accuracy: Double,
    val startTimeMs: Long,
    val endTimeMs: Long,
    val wordDetails: ImmutableList<WordAnalysisUiModel>,
) {
    val isScriptMatchIssue: Boolean
        get() = wordDetails.any { word -> word.status.isScriptMatchIssue }

    val isSpeechAccuracyIssue: Boolean
        get() = wordDetails.any { word -> word.status.isSpeechAccuracyIssue }

    val scriptMatchStatus: WordAnalysisStatus
        get() = wordDetails.firstOrNull { word -> word.status.isScriptMatchIssue }?.status ?: status

    val speechAccuracyStatus: WordAnalysisStatus
        get() = wordDetails.firstOrNull { word -> word.status.isSpeechAccuracyIssue }?.status ?: status
}

@Immutable
internal data class WordAnalysisUiModel(
    val word: String,
    val status: WordAnalysisStatus,
    val accuracy: Double,
    val startTimeMs: Long,
    val endTimeMs: Long,
)

private val WordAnalysisStatus.isScriptMatchIssue: Boolean
    get() = this == WordAnalysisStatus.INSERTION ||
        this == WordAnalysisStatus.OMISSION ||
        this == WordAnalysisStatus.MISPRONUNCIATION

private val WordAnalysisStatus.isSpeechAccuracyIssue: Boolean
    get() = this == WordAnalysisStatus.EXCELLENT ||
        this == WordAnalysisStatus.GOOD ||
        this == WordAnalysisStatus.STUTTER

internal fun ImmutableList<SentenceAnalysisDetail>.toUiModels(): ImmutableList<SentenceAnalysisUiModel> =
    map { detail -> detail.toUiModel() }.toImmutableList()

private fun SentenceAnalysisDetail.toUiModel(): SentenceAnalysisUiModel =
    SentenceAnalysisUiModel(
        sentence = sentence,
        status = status,
        mainFeedback = mainFeedback,
        subFeedback = subFeedback,
        accuracy = accuracy,
        startTimeMs = startTimeMs,
        endTimeMs = endTimeMs,
        wordDetails = wordDetails.map { word -> word.toUiModel() }.toImmutableList(),
    )

private fun WordAnalysisDetail.toUiModel(): WordAnalysisUiModel =
    WordAnalysisUiModel(
        word = word,
        status = status,
        accuracy = accuracy,
        startTimeMs = startTimeMs,
        endTimeMs = endTimeMs,
    )
