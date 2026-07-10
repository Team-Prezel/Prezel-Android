package com.team.prezel.feature.report.impl.accuracydetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.team.prezel.core.designsystem.component.player.PrezelPlayerMarkerType
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.WordAnalysisStatus
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.accuracydetail.model.SentenceAnalysisUiModel
import kotlinx.collections.immutable.persistentListOf

internal fun WordAnalysisStatus.toMarkerType(): PrezelPlayerMarkerType =
    when (this) {
        WordAnalysisStatus.EXCELLENT,
        WordAnalysisStatus.GOOD,
        -> PrezelPlayerMarkerType.GOOD

        WordAnalysisStatus.OMISSION -> PrezelPlayerMarkerType.NEUTRAL

        else -> PrezelPlayerMarkerType.WARNING
    }

@Composable
private fun WordAnalysisStatus.textColor(): Color =
    when (this) {
        WordAnalysisStatus.EXCELLENT -> PrezelTheme.colors.interactiveRegular
        else -> PrezelTheme.colors.textLarge
    }

@Composable
internal fun SentenceAnalysisCard(
    detail: SentenceAnalysisUiModel,
    highlighted: Boolean,
    modifier: Modifier = Modifier,
    text: String = detail.sentence,
    useStatusTextColor: Boolean = true,
    showStatusChip: Boolean = true,
    subText: String? = null,
    highlightWordDetails: Boolean = false,
    status: WordAnalysisStatus = detail.status,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(PrezelTheme.shapes.V8)
            .background(if (highlighted) PrezelTheme.colors.bgMedium else Color.Transparent)
            .padding(PrezelTheme.spacing.V12),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = detail.startTimeMs.toPlayerTimeText(),
                style = PrezelTheme.typography.caption1Regular,
                color = PrezelTheme.colors.textRegular,
            )
            if (showStatusChip) {
                StatusChip(status = status)
            }
        }
        if (highlightWordDetails) {
            SentenceText(detail = detail, text = text)
        } else {
            Text(
                text = text,
                style = PrezelTheme.typography.body2Medium,
                color = if (useStatusTextColor) status.textColor() else PrezelTheme.colors.textLarge,
            )
        }
        subText?.takeIf { it.isNotBlank() }?.let { feedback ->
            Text(
                text = feedback,
                style = PrezelTheme.typography.body3Regular,
                color = PrezelTheme.colors.textRegular,
            )
        }
    }
}

@Composable
private fun SentenceText(
    detail: SentenceAnalysisUiModel,
    text: String,
) {
    val defaultColor = PrezelTheme.colors.textLarge
    val successColor = PrezelTheme.colors.interactiveRegular
    val warningColor = PrezelTheme.colors.feedbackWarningRegular
    val neutralColor = PrezelTheme.colors.textRegular
    var searchFrom = 0
    val spans = detail.wordDetails.mapNotNull { word ->
        text
            .indexOf(word.word, startIndex = searchFrom)
            .takeIf { start -> start >= 0 }
            ?.let { start ->
                searchFrom = start + word.word.length
                WordSpan(
                    start = start,
                    end = searchFrom,
                    color = word.status.textColor(
                        successColor = successColor,
                        warningColor = warningColor,
                        neutralColor = neutralColor,
                    ),
                )
            }
    }

    Text(
        text = buildAnnotatedString {
            var cursor = 0
            spans.forEach { span ->
                if (span.start < cursor) return@forEach
                append(text.substring(cursor, span.start))
                withStyle(SpanStyle(color = span.color)) {
                    append(text.substring(span.start, span.end))
                }
                cursor = span.end
            }
            append(text.substring(cursor))
        },
        style = PrezelTheme.typography.body2Medium,
        color = defaultColor,
    )
}

private data class WordSpan(
    val start: Int,
    val end: Int,
    val color: Color,
)

private fun WordAnalysisStatus.textColor(
    successColor: Color,
    warningColor: Color,
    neutralColor: Color,
): Color =
    when (this) {
        WordAnalysisStatus.EXCELLENT,
        WordAnalysisStatus.GOOD,
        -> successColor

        WordAnalysisStatus.INSERTION,
        WordAnalysisStatus.MISPRONUNCIATION,
        WordAnalysisStatus.STUTTER,
        -> warningColor

        else -> neutralColor
    }

@Composable
internal fun StatusChip(status: WordAnalysisStatus) {
    Text(
        text = status.statusLabel(),
        style = PrezelTheme.typography.body3Medium,
        color = status.chipTextColor(),
        modifier = Modifier
            .clip(PrezelTheme.shapes.V4)
            .background(status.chipBackgroundColor())
            .padding(horizontal = PrezelTheme.spacing.V6, vertical = PrezelTheme.spacing.V2),
    )
}

@Composable
private fun WordAnalysisStatus.statusLabel(): String =
    when (this) {
        WordAnalysisStatus.EXCELLENT,
        WordAnalysisStatus.GOOD,
        WordAnalysisStatus.STUTTER,
        -> stringResource(R.string.feature_report_impl_script_detail_status_pronunciation)

        WordAnalysisStatus.INSERTION -> stringResource(R.string.feature_report_impl_script_detail_status_insertion)
        WordAnalysisStatus.OMISSION -> stringResource(R.string.feature_report_impl_script_detail_status_omission)
        WordAnalysisStatus.MISPRONUNCIATION -> stringResource(R.string.feature_report_impl_script_detail_status_mismatch)
        WordAnalysisStatus.UNKNOWN -> stringResource(R.string.feature_report_impl_script_detail_status_unknown)
    }

@Composable
private fun WordAnalysisStatus.chipTextColor(): Color =
    when (this) {
        WordAnalysisStatus.INSERTION,
        WordAnalysisStatus.MISPRONUNCIATION,
        WordAnalysisStatus.STUTTER,
        -> PrezelTheme.colors.feedbackWarningRegular

        WordAnalysisStatus.OMISSION -> PrezelTheme.colors.textRegular
        else -> PrezelTheme.colors.interactiveRegular
    }

@Composable
private fun WordAnalysisStatus.chipBackgroundColor(): Color =
    when (this) {
        WordAnalysisStatus.INSERTION,
        WordAnalysisStatus.MISPRONUNCIATION,
        WordAnalysisStatus.STUTTER,
        -> PrezelTheme.colors.feedbackWarningSmall

        WordAnalysisStatus.OMISSION -> PrezelTheme.colors.bgLarge
        else -> PrezelTheme.colors.interactiveXSmall
    }

internal fun Long.toPlayerTimeText(): String {
    val totalSeconds = coerceAtLeast(0L) / 1_000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

@BasicPreview
@Composable
private fun StatusChipPreview() {
    PrezelTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8)) {
            StatusChip(status = PreviewExcellentWord.speechAccuracyStatus)
            StatusChip(status = PreviewInsertionWord.scriptMatchStatus)
            StatusChip(
                status = SentenceAnalysisUiModel(
                    sentence = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
                    status = WordAnalysisStatus.OMISSION,
                    mainFeedback = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
                    subFeedback = "대본에 있으나 읽지 않은 구간이에요.",
                    accuracy = 0.0,
                    startTimeMs = 9_400L,
                    endTimeMs = 11_300L,
                    wordDetails = persistentListOf(),
                ).scriptMatchStatus,
            )
        }
    }
}

@BasicPreview
@Composable
private fun SentenceAnalysisCardPreview() {
    PrezelTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
            modifier = Modifier.padding(PrezelTheme.spacing.V16),
        ) {
            SentenceAnalysisCard(
                detail = PreviewExcellentWord,
                highlighted = true,
            )
            SentenceAnalysisCard(
                detail = PreviewInsertionWord,
                highlighted = false,
                text = PreviewInsertionWord.mainFeedback,
                subText = PreviewInsertionWord.subFeedback,
            )
        }
    }
}

private val PreviewExcellentWord = SentenceAnalysisUiModel(
    sentence = "문장의 흐름이 깔끔했어요",
    status = WordAnalysisStatus.EXCELLENT,
    mainFeedback = "문장의 흐름이 깔끔했어요",
    subFeedback = "지금처럼 또렷한 말하기를 유지해주세요.",
    accuracy = 96.0,
    startTimeMs = 0L,
    endTimeMs = 1_800L,
    wordDetails = persistentListOf(),
)

private val PreviewInsertionWord = SentenceAnalysisUiModel(
    sentence = "같은 말을 반복하고 있어요.",
    status = WordAnalysisStatus.INSERTION,
    mainFeedback = "같은 말을 반복하고 있어요.",
    subFeedback = "앞에서 했던 말은 반복하지 않는 것이 좋아요.",
    accuracy = 42.0,
    startTimeMs = 7_230L,
    endTimeMs = 8_700L,
    wordDetails = persistentListOf(),
)
