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
import com.team.prezel.core.designsystem.component.player.PrezelPlayerMarkerType
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.WordAnalysisDetail
import com.team.prezel.core.model.presentation.WordAnalysisStatus
import com.team.prezel.feature.report.impl.R

internal val WordAnalysisDetail.isScriptMatchIssue: Boolean
    get() = status == WordAnalysisStatus.OMISSION || status == WordAnalysisStatus.MISPRONUNCIATION

internal val WordAnalysisDetail.isSpeechAccuracyIssue: Boolean
    get() = status == WordAnalysisStatus.EXCELLENT ||
        status == WordAnalysisStatus.GOOD ||
        status == WordAnalysisStatus.STUTTER ||
        status == WordAnalysisStatus.INSERTION

internal val WordAnalysisDetail.isSpeechAccuracySheetIssue: Boolean
    get() = status == WordAnalysisStatus.EXCELLENT ||
        status == WordAnalysisStatus.STUTTER ||
        status == WordAnalysisStatus.INSERTION

internal fun WordAnalysisDetail.toMarkerType(): PrezelPlayerMarkerType =
    when (status) {
        WordAnalysisStatus.EXCELLENT,
        WordAnalysisStatus.GOOD,
        -> PrezelPlayerMarkerType.GOOD

        WordAnalysisStatus.OMISSION -> PrezelPlayerMarkerType.NEUTRAL

        else -> PrezelPlayerMarkerType.WARNING
    }

@Composable
internal fun WordAnalysisDetail.textColor(): Color =
    when (status) {
        WordAnalysisStatus.EXCELLENT -> PrezelTheme.colors.interactiveRegular
        else -> PrezelTheme.colors.textLarge
    }

@Composable
internal fun WordDetailCard(
    detail: WordAnalysisDetail,
    highlighted: Boolean,
    modifier: Modifier = Modifier,
    text: String = detail.word,
    useStatusTextColor: Boolean = true,
    showStatusChip: Boolean = true,
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
                StatusChip(detail = detail)
            }
        }
        Text(
            text = text,
            style = PrezelTheme.typography.body2Medium,
            color = if (useStatusTextColor) detail.textColor() else PrezelTheme.colors.textLarge,
        )
    }
}

@Composable
internal fun StatusChip(detail: WordAnalysisDetail) {
    Text(
        text = detail.statusLabel(),
        style = PrezelTheme.typography.body3Medium,
        color = detail.chipTextColor(),
        modifier = Modifier
            .clip(PrezelTheme.shapes.V4)
            .background(detail.chipBackgroundColor())
            .padding(horizontal = PrezelTheme.spacing.V6, vertical = PrezelTheme.spacing.V2),
    )
}

@Composable
private fun WordAnalysisDetail.statusLabel(): String =
    when (status) {
        WordAnalysisStatus.EXCELLENT,
        WordAnalysisStatus.GOOD,
        WordAnalysisStatus.STUTTER,
        -> stringResource(R.string.feature_report_impl_script_detail_status_pronunciation)

        WordAnalysisStatus.INSERTION -> stringResource(R.string.feature_report_impl_script_detail_status_insertion)
        WordAnalysisStatus.OMISSION -> stringResource(R.string.feature_report_impl_script_detail_status_omission)
        WordAnalysisStatus.MISPRONUNCIATION -> stringResource(R.string.feature_report_impl_script_detail_status_mismatch)
        WordAnalysisStatus.UNKNOWN -> status.value
    }

@Composable
private fun WordAnalysisDetail.chipTextColor(): Color =
    when (status) {
        WordAnalysisStatus.INSERTION,
        WordAnalysisStatus.MISPRONUNCIATION,
        -> PrezelTheme.colors.feedbackWarningRegular

        WordAnalysisStatus.OMISSION -> PrezelTheme.colors.textRegular
        else -> PrezelTheme.colors.interactiveRegular
    }

@Composable
private fun WordAnalysisDetail.chipBackgroundColor(): Color =
    when (status) {
        WordAnalysisStatus.INSERTION,
        WordAnalysisStatus.MISPRONUNCIATION,
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
            StatusChip(detail = PreviewExcellentWord)
            StatusChip(detail = PreviewInsertionWord)
            StatusChip(detail = PreviewOmissionWord)
        }
    }
}

@BasicPreview
@Composable
private fun WordDetailCardPreview() {
    PrezelTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
            modifier = Modifier.padding(PrezelTheme.spacing.V16),
        ) {
            WordDetailCard(
                detail = PreviewExcellentWord,
                highlighted = true,
            )
            WordDetailCard(
                detail = PreviewInsertionWord,
                highlighted = false,
                text = PreviewInsertionWord.description,
            )
        }
    }
}

private val PreviewExcellentWord = WordAnalysisDetail(
    word = "문장의 흐름이 깔끔했어요",
    status = WordAnalysisStatus.EXCELLENT,
    description = "지금처럼 또렷한 말하기를 유지해주세요.",
    accuracy = 96.0,
    startTimeMs = 0L,
    endTimeMs = 1_800L,
)

private val PreviewInsertionWord = WordAnalysisDetail(
    word = "같은 말을 반복하고 있어요.",
    status = WordAnalysisStatus.INSERTION,
    description = "앞에서 했던 말은 반복하지 않는 것이 좋아요.",
    accuracy = 42.0,
    startTimeMs = 7_230L,
    endTimeMs = 8_700L,
)

private val PreviewOmissionWord = WordAnalysisDetail(
    word = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
    status = WordAnalysisStatus.OMISSION,
    description = "대본에 있으나 읽지 않은 구간이에요.",
    accuracy = 0.0,
    startTimeMs = 9_400L,
    endTimeMs = 11_300L,
)
