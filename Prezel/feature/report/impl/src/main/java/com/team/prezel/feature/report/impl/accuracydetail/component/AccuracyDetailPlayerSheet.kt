package com.team.prezel.feature.report.impl.accuracydetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.player.PrezelPlayer
import com.team.prezel.core.designsystem.component.player.PrezelPlayerItem
import com.team.prezel.core.designsystem.component.player.PrezelPlayerState
import com.team.prezel.core.designsystem.component.player.rememberPrezelPlayerState
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.WordAnalysisDetail
import com.team.prezel.core.model.presentation.WordAnalysisStatus
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.accuracydetail.AccuracyDetailTab
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun AccuracyDetailPlayerSheet(
    selectedTab: AccuracyDetailTab,
    selectedWord: WordAnalysisDetail?,
    wordDetails: List<WordAnalysisDetail>,
    playerState: PrezelPlayerState,
    expanded: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (expanded) Modifier.fillMaxHeight() else Modifier),
    ) {
        SheetHandle()
        SheetDetailContent(
            selectedTab = selectedTab,
            selectedWord = selectedWord,
            wordDetails = wordDetails,
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
    selectedWord: WordAnalysisDetail?,
    wordDetails: List<WordAnalysisDetail>,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(if (expanded) Modifier else Modifier.heightIn(max = 96.dp))
            .padding(horizontal = PrezelTheme.spacing.V20)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
    ) {
        when (selectedTab) {
            AccuracyDetailTab.SPEECH -> SpeechDetailContent(
                selectedWord = selectedWord,
                wordDetails = wordDetails,
                expanded = expanded,
            )

            AccuracyDetailTab.SCRIPT_MATCH -> ScriptMatchDetailContent(
                selectedWord = selectedWord,
                wordDetails = wordDetails,
                expanded = expanded,
            )
        }
    }
    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))
}

@Composable
private fun SpeechDetailContent(
    selectedWord: WordAnalysisDetail?,
    wordDetails: List<WordAnalysisDetail>,
    expanded: Boolean,
) {
    val accuracyDetails = wordDetails.filter { it.isSpeechAccuracySheetIssue }
    val visibleAccuracyDetails = if (expanded) {
        accuracyDetails
    } else {
        listOfNotNull(selectedWord?.takeIf { it.isSpeechAccuracySheetIssue } ?: accuracyDetails.firstOrNull())
    }

    if (visibleAccuracyDetails.isEmpty()) {
        EmptyDetailText(text = stringResource(R.string.feature_report_impl_accuracy_detail_sheet_empty_speech))
    } else {
        visibleAccuracyDetails.forEach { detail ->
            WordDetailCard(
                detail = detail,
                highlighted = detail == selectedWord,
                text = detail.description,
                useStatusTextColor = false,
            )
        }
    }
}

@Composable
private fun ScriptMatchDetailContent(
    selectedWord: WordAnalysisDetail?,
    wordDetails: List<WordAnalysisDetail>,
    expanded: Boolean,
) {
    val visibleMismatchDetails = wordDetails.visibleScriptMatchDetails(
        selectedWord = selectedWord,
        expanded = expanded,
    )

    if (visibleMismatchDetails.isEmpty()) {
        EmptyDetailText(text = stringResource(R.string.feature_report_impl_accuracy_detail_sheet_empty_script))
    }

    visibleMismatchDetails.forEach { detail ->
        WordDetailCard(
            detail = detail,
            highlighted = detail == selectedWord,
            text = detail.description,
            useStatusTextColor = false,
        )
    }
}

private fun List<WordAnalysisDetail>.visibleScriptMatchDetails(
    selectedWord: WordAnalysisDetail?,
    expanded: Boolean,
): List<WordAnalysisDetail> {
    val mismatchDetails = filter { it.isScriptMatchIssue }
    return if (expanded) {
        mismatchDetails
    } else {
        listOfNotNull(selectedWord?.takeIf { it.isScriptMatchIssue } ?: mismatchDetails.firstOrNull())
    }
}

@BasicPreview
@Composable
private fun AccuracyDetailPlayerSheetSpeechPreview() {
    PrezelTheme {
        AccuracyDetailPlayerSheet(
            selectedTab = AccuracyDetailTab.SPEECH,
            selectedWord = PreviewWordDetails.first(),
            wordDetails = PreviewWordDetails,
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
            selectedWord = PreviewWordDetails[1],
            wordDetails = PreviewWordDetails,
            playerState = rememberPreviewPlayerState(),
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
            selectedWord = PreviewWordDetails[1],
            wordDetails = PreviewWordDetails,
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
        initialItems = PreviewWordDetails
            .map { detail ->
                PrezelPlayerItem.Marker(
                    timeMillis = detail.startTimeMs,
                    markerType = detail.toMarkerType(),
                )
            }.toImmutableList(),
    )

private val PreviewWordDetails = listOf(
    WordAnalysisDetail(
        word = "문장의 흐름이 깔끔했어요",
        status = WordAnalysisStatus.EXCELLENT,
        description = "지금처럼 또렷한 말하기를 유지해주세요.",
        accuracy = 96.0,
        startTimeMs = 0L,
        endTimeMs = 1_800L,
    ),
    WordAnalysisDetail(
        word = "같은 말을 반복하고 있어요.",
        status = WordAnalysisStatus.INSERTION,
        description = "앞에서 했던 말은 반복하지 않는 것이 좋아요.",
        accuracy = 42.0,
        startTimeMs = 7_230L,
        endTimeMs = 8_700L,
    ),
    WordAnalysisDetail(
        word = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
        status = WordAnalysisStatus.OMISSION,
        description = "대본에 있으나 읽지 않은 구간이에요.",
        accuracy = 0.0,
        startTimeMs = 9_400L,
        endTimeMs = 11_300L,
    ),
)
