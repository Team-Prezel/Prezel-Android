package com.team.prezel.feature.report.impl.accuracydetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.chip.chip.ChipHierarchy
import com.team.prezel.core.designsystem.component.chip.chip.ChipSize
import com.team.prezel.core.designsystem.component.chip.chip.PrezelChip
import com.team.prezel.core.designsystem.component.player.PrezelPlayer
import com.team.prezel.core.designsystem.component.player.PrezelPlayerItem
import com.team.prezel.core.designsystem.component.player.PrezelPlayerState
import com.team.prezel.core.designsystem.component.player.rememberPrezelPlayerState
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.WordAnalysisStatus
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.accuracydetail.AccuracyDetailTab
import com.team.prezel.feature.report.impl.accuracydetail.model.SentenceAnalysisUiModel
import com.team.prezel.feature.report.impl.accuracydetail.model.WordAnalysisUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun AccuracyDetailPlayerSheet(
    selectedTab: AccuracyDetailTab,
    selectedSentence: SentenceAnalysisUiModel?,
    sentenceDetails: ImmutableList<SentenceAnalysisUiModel>,
    playerState: PrezelPlayerState,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.then(
            if (expanded) {
                Modifier.fillMaxSize()
            } else {
                Modifier.fillMaxWidth()
            },
        ),
    ) {
        SheetHandle()
        SheetDetailContent(
            selectedTab = selectedTab,
            selectedSentence = selectedSentence,
            sentenceDetails = sentenceDetails,
            expanded = expanded,
            modifier = if (expanded) Modifier.weight(1f) else Modifier,
        )
        PrezelPlayer(
            state = playerState,
            trackContentDescription = stringResource(R.string.feature_report_impl_script_detail_player_track_desc),
        )
    }
}

@Composable
internal fun SheetHandle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PrezelTheme.spacing.V16),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(width = 32.dp, height = 4.dp)
                .clip(PrezelTheme.shapes.V1000)
                .background(PrezelTheme.colors.borderMedium),
        )
    }
}

@Composable
private fun SheetDetailContent(
    selectedTab: AccuracyDetailTab,
    selectedSentence: SentenceAnalysisUiModel?,
    sentenceDetails: ImmutableList<SentenceAnalysisUiModel>,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    val emptyDetailTextResId = when (selectedTab) {
        AccuracyDetailTab.SPEECH -> R.string.feature_report_impl_accuracy_detail_sheet_empty_speech
        AccuracyDetailTab.SCRIPT_MATCH -> R.string.feature_report_impl_accuracy_detail_sheet_empty_script
    }
    val isEmptyDetail = when (selectedTab) {
        AccuracyDetailTab.SPEECH -> sentenceDetails.visibleSpeechAccuracyDetails(
            selectedSentence = selectedSentence,
            expanded = expanded,
        )

        AccuracyDetailTab.SCRIPT_MATCH -> sentenceDetails.visibleScriptMatchDetails(
            selectedSentence = selectedSentence,
            expanded = expanded,
        )
    }.isEmpty()

    if (expanded && isEmptyDetail) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = PrezelTheme.spacing.V20),
            contentAlignment = Alignment.Center,
        ) {
            ExpandedEmptyDetailText(text = stringResource(emptyDetailTextResId))
        }
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = PrezelTheme.spacing.V20)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
    ) {
        if (isEmptyDetail) {
            CollapsedEmptyDetailCard(text = stringResource(emptyDetailTextResId))
        } else {
            when (selectedTab) {
                AccuracyDetailTab.SPEECH -> SpeechDetailContent(
                    selectedSentence = selectedSentence,
                    sentenceDetails = sentenceDetails,
                    expanded = expanded,
                )

                AccuracyDetailTab.SCRIPT_MATCH -> ScriptMatchDetailContent(
                    selectedSentence = selectedSentence,
                    sentenceDetails = sentenceDetails,
                    expanded = expanded,
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))
}

@Composable
private fun CollapsedEmptyDetailCard(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(PrezelTheme.shapes.V8)
            .background(PrezelTheme.colors.bgMedium)
            .padding(PrezelTheme.spacing.V12),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
    ) {
        PrezelChip(
            text = stringResource(R.string.feature_report_impl_accuracy_detail_sheet_empty_card_label),
            size = ChipSize.SMALL,
            hierarchy = ChipHierarchy.SECONDARY,
        )
        Text(
            text = text,
            style = PrezelTheme.typography.body2Medium,
            color = PrezelTheme.colors.textLarge,
        )
    }
}

@Composable
private fun ExpandedEmptyDetailText(text: String) {
    Text(
        text = text,
        style = PrezelTheme.typography.body3Medium,
        color = PrezelTheme.colors.textMedium,
    )
}

@Composable
private fun SpeechDetailContent(
    selectedSentence: SentenceAnalysisUiModel?,
    sentenceDetails: ImmutableList<SentenceAnalysisUiModel>,
    expanded: Boolean,
) {
    val visibleAccuracyDetails = sentenceDetails.visibleSpeechAccuracyDetails(
        selectedSentence = selectedSentence,
        expanded = expanded,
    )

    if (visibleAccuracyDetails.isEmpty()) {
        EmptyDetailText(text = stringResource(R.string.feature_report_impl_accuracy_detail_sheet_empty_speech))
    } else {
        visibleAccuracyDetails.forEach { detail ->
            SentenceAnalysisCard(
                detail = detail,
                highlighted = detail == selectedSentence,
                text = detail.mainFeedback,
                subText = detail.subFeedback.takeIf { expanded },
                useStatusTextColor = false,
                status = detail.speechAccuracyStatus,
            )
        }
    }
}

private fun ImmutableList<SentenceAnalysisUiModel>.visibleSpeechAccuracyDetails(
    selectedSentence: SentenceAnalysisUiModel?,
    expanded: Boolean,
): ImmutableList<SentenceAnalysisUiModel> {
    val accuracyDetails = filter { it.isSpeechAccuracyIssue }.toImmutableList()
    return if (expanded) {
        accuracyDetails
    } else {
        listOfNotNull(selectedSentence?.takeIf { it.isSpeechAccuracyIssue } ?: accuracyDetails.firstOrNull()).toImmutableList()
    }
}

@Composable
private fun ScriptMatchDetailContent(
    selectedSentence: SentenceAnalysisUiModel?,
    sentenceDetails: ImmutableList<SentenceAnalysisUiModel>,
    expanded: Boolean,
) {
    val visibleMismatchDetails = sentenceDetails.visibleScriptMatchDetails(
        selectedSentence = selectedSentence,
        expanded = expanded,
    )

    if (visibleMismatchDetails.isEmpty()) {
        EmptyDetailText(text = stringResource(R.string.feature_report_impl_accuracy_detail_sheet_empty_script))
    }

    visibleMismatchDetails.forEach { detail ->
        SentenceAnalysisCard(
            detail = detail,
            highlighted = detail == selectedSentence,
            text = detail.mainFeedback,
            subText = detail.subFeedback.takeIf { expanded },
            useStatusTextColor = false,
            status = detail.scriptMatchStatus,
        )
    }
}

private fun ImmutableList<SentenceAnalysisUiModel>.visibleScriptMatchDetails(
    selectedSentence: SentenceAnalysisUiModel?,
    expanded: Boolean,
): ImmutableList<SentenceAnalysisUiModel> {
    val mismatchDetails = filter { it.isScriptMatchIssue }.toImmutableList()
    return if (expanded) {
        mismatchDetails
    } else {
        listOfNotNull(selectedSentence?.takeIf { it.isScriptMatchIssue } ?: mismatchDetails.firstOrNull()).toImmutableList()
    }
}

@BasicPreview
@Composable
private fun AccuracyDetailPlayerSheetSpeechPreview() {
    PrezelTheme {
        AccuracyDetailPlayerSheet(
            selectedTab = AccuracyDetailTab.SPEECH,
            selectedSentence = PreviewSentenceDetails.first(),
            sentenceDetails = PreviewSentenceDetails,
            playerState = rememberPreviewPlayerState(),
            expanded = false,
        )
    }
}

@BasicPreview
@Composable
private fun AccuracyDetailPlayerSheetScriptMatchPreview() {
    PrezelTheme {
        AccuracyDetailPlayerSheet(
            selectedTab = AccuracyDetailTab.SCRIPT_MATCH,
            selectedSentence = PreviewSentenceDetails[1],
            sentenceDetails = PreviewSentenceDetails,
            playerState = rememberPreviewPlayerState(),
            expanded = false,
        )
    }
}

@BasicPreview
@Composable
private fun AccuracyDetailPlayerSheetAllAccuratePreview() {
    PrezelTheme {
        AccuracyDetailPlayerSheet(
            selectedTab = AccuracyDetailTab.SPEECH,
            selectedSentence = PreviewAllAccurateSentenceDetails.first(),
            sentenceDetails = PreviewAllAccurateSentenceDetails,
            playerState = rememberPrezelPlayerState(
                durationMillis = 11_300L,
                currentMillis = 0L,
                initialItems = persistentListOf(),
            ),
            expanded = false,
        )
    }
}

@BasicPreview
@Composable
private fun AccuracyDetailPlayerSheetAllScriptMatchedPreview() {
    PrezelTheme {
        AccuracyDetailPlayerSheet(
            selectedTab = AccuracyDetailTab.SCRIPT_MATCH,
            selectedSentence = PreviewAllAccurateSentenceDetails.first(),
            sentenceDetails = PreviewAllAccurateSentenceDetails,
            playerState = rememberPrezelPlayerState(
                durationMillis = 11_300L,
                currentMillis = 7_230L,
                initialItems = persistentListOf(),
            ),
            expanded = false,
        )
    }
}

@BasicPreview
@Composable
private fun AccuracyDetailPlayerSheetExpandedPreview() {
    PrezelTheme {
        AccuracyDetailPlayerSheet(
            selectedTab = AccuracyDetailTab.SPEECH,
            selectedSentence = PreviewSentenceDetails[1],
            sentenceDetails = PreviewSentenceDetails,
            playerState = rememberPreviewPlayerState(currentMillis = 7_230L),
            expanded = true,
        )
    }
}

@Composable
private fun rememberPreviewPlayerState(currentMillis: Long = 0L): PrezelPlayerState =
    rememberPrezelPlayerState(
        durationMillis = 11_300L,
        currentMillis = currentMillis,
        initialItems = PreviewSentenceDetails
            .map { detail ->
                PrezelPlayerItem.Marker(
                    timeMillis = detail.startTimeMs,
                    markerType = detail.speechAccuracyStatus.toMarkerType(),
                )
            }.toImmutableList(),
    )

private val PreviewSentenceDetails = persistentListOf(
    SentenceAnalysisUiModel(
        sentence = "문장의 흐름이 깔끔했어요",
        status = WordAnalysisStatus.EXCELLENT,
        mainFeedback = "문장의 흐름이 깔끔했어요",
        subFeedback = "지금처럼 또렷한 말하기를 유지해주세요.",
        accuracy = 96.0,
        startTimeMs = 0L,
        endTimeMs = 1_800L,
        wordDetails = persistentListOf(
            WordAnalysisUiModel(
                word = "흐름이",
                status = WordAnalysisStatus.EXCELLENT,
                accuracy = 96.0,
                startTimeMs = 320L,
                endTimeMs = 780L,
            ),
        ),
    ),
    SentenceAnalysisUiModel(
        sentence = "같은 말을 반복하고 있어요.",
        status = WordAnalysisStatus.INSERTION,
        mainFeedback = "같은 말을 반복하고 있어요.",
        subFeedback = "앞에서 했던 말은 반복하지 않는 것이 좋아요. 다시 한 번 또박또박 연습해보세요.",
        accuracy = 42.0,
        startTimeMs = 7_230L,
        endTimeMs = 8_700L,
        wordDetails = persistentListOf(
            WordAnalysisUiModel(
                word = "반복하고",
                status = WordAnalysisStatus.INSERTION,
                accuracy = 42.0,
                startTimeMs = 7_230L,
                endTimeMs = 7_900L,
            ),
        ),
    ),
    SentenceAnalysisUiModel(
        sentence = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
        status = WordAnalysisStatus.OMISSION,
        mainFeedback = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
        subFeedback = "대본에 있으나 읽지 않은 구간이에요.",
        accuracy = 0.0,
        startTimeMs = 9_400L,
        endTimeMs = 11_300L,
        wordDetails = persistentListOf(
            WordAnalysisUiModel(
                word = "오늘도",
                status = WordAnalysisStatus.OMISSION,
                accuracy = 0.0,
                startTimeMs = 9_400L,
                endTimeMs = 10_100L,
            ),
        ),
    ),
)

private val PreviewAllAccurateSentenceDetails = persistentListOf(
    SentenceAnalysisUiModel(
        sentence = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
        status = WordAnalysisStatus.EXCELLENT,
        mainFeedback = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
        subFeedback = "모든 단어가 정확하게 발음되었어요.",
        accuracy = 98.0,
        startTimeMs = 0L,
        endTimeMs = 6_800L,
        wordDetails = persistentListOf(
            WordAnalysisUiModel(
                word = "발음",
                status = WordAnalysisStatus.EXCELLENT,
                accuracy = 99.0,
                startTimeMs = 1_200L,
                endTimeMs = 1_700L,
            ),
        ),
    ),
    SentenceAnalysisUiModel(
        sentence = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
        status = WordAnalysisStatus.GOOD,
        mainFeedback = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
        subFeedback = "모든 단어가 안정적으로 전달되었어요.",
        accuracy = 94.0,
        startTimeMs = 7_000L,
        endTimeMs = 8_800L,
        wordDetails = persistentListOf(),
    ),
    SentenceAnalysisUiModel(
        sentence = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
        status = WordAnalysisStatus.EXCELLENT,
        mainFeedback = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
        subFeedback = "또렷한 발음을 유지해주세요.",
        accuracy = 97.0,
        startTimeMs = 9_000L,
        endTimeMs = 11_300L,
        wordDetails = persistentListOf(),
    ),
)
