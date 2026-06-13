package com.team.prezel.feature.report.impl.accuracydetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.WordAnalysisStatus
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.accuracydetail.AccuracyDetailTab
import com.team.prezel.feature.report.impl.accuracydetail.model.SentenceAnalysisUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ScriptDetailList(
    selectedTab: AccuracyDetailTab,
    selectedSentence: SentenceAnalysisUiModel?,
    sentenceDetails: ImmutableList<SentenceAnalysisUiModel>,
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(selectedTab) {
        scrollState.scrollTo(0)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(all = PrezelTheme.spacing.V20),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
    ) {
        if (sentenceDetails.isNotEmpty()) {
            sentenceDetails.forEach { detail ->
                SentenceAnalysisCard(
                    detail = detail,
                    highlighted = detail == selectedSentence,
                    showStatusChip = detail.showsStatusChip(selectedTab),
                    highlightWordDetails = true,
                    status = detail.statusFor(selectedTab),
                )
            }
        } else {
            EmptyDetailText(text = stringResource(R.string.feature_report_impl_script_detail_empty_speech))
        }
    }
}

private fun SentenceAnalysisUiModel.showsStatusChip(selectedTab: AccuracyDetailTab): Boolean =
    when (selectedTab) {
        AccuracyDetailTab.SPEECH -> hasSpeechAccuracyStatus
        AccuracyDetailTab.SCRIPT_MATCH -> isScriptMatchIssue
    }

private fun SentenceAnalysisUiModel.statusFor(selectedTab: AccuracyDetailTab): WordAnalysisStatus =
    when (selectedTab) {
        AccuracyDetailTab.SPEECH -> speechAccuracyStatus
        AccuracyDetailTab.SCRIPT_MATCH -> scriptMatchStatus
    }

@Composable
internal fun EmptyDetailText(text: String) {
    Text(
        text = text,
        style = PrezelTheme.typography.body2Regular,
        color = PrezelTheme.colors.textRegular,
    )
}

@BasicPreview
@Composable
private fun ScriptDetailListPreview() {
    PrezelTheme {
        ScriptDetailList(
            selectedTab = AccuracyDetailTab.SPEECH,
            selectedSentence = PreviewSentenceDetail,
            sentenceDetails = PreviewSentenceDetails,
        )
    }
}

@BasicPreview
@Composable
private fun ScriptDetailListEmptyPreview() {
    PrezelTheme {
        ScriptDetailList(
            selectedTab = AccuracyDetailTab.SPEECH,
            selectedSentence = null,
            sentenceDetails = persistentListOf(),
        )
    }
}

private val PreviewSentenceDetail = SentenceAnalysisUiModel(
    sentence = "문장의 흐름이 깔끔했어요.",
    status = WordAnalysisStatus.EXCELLENT,
    mainFeedback = "문장의 흐름이 깔끔했어요.",
    subFeedback = "지금처럼 또렷한 말하기를 유지해주세요.",
    accuracy = 98.0,
    startTimeMs = 1_490L,
    endTimeMs = 1_980L,
    wordDetails = persistentListOf(),
)

private val PreviewSentenceDetails = persistentListOf(
    PreviewSentenceDetail,
    SentenceAnalysisUiModel(
        sentence = "같은 말을 반복하고 있어요.",
        status = WordAnalysisStatus.EXCELLENT,
        mainFeedback = "같은 말을 반복하고 있어요.",
        subFeedback = "앞에서 했던 말은 반복하지 않는 것이 좋아요.",
        accuracy = 98.0,
        startTimeMs = 1_990L,
        endTimeMs = 2_400L,
        wordDetails = persistentListOf(),
    ),
    SentenceAnalysisUiModel(
        sentence = "문장이 끝까지 정확하게 전달돼요.",
        status = WordAnalysisStatus.EXCELLENT,
        mainFeedback = "문장이 끝까지 정확하게 전달돼요.",
        subFeedback = "말의 마무리가 깔끔해 신뢰감이 높게 들려요.",
        accuracy = 100.0,
        startTimeMs = 2_410L,
        endTimeMs = 2_820L,
        wordDetails = persistentListOf(),
    ),
)
