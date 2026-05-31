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
import com.team.prezel.core.model.presentation.WordAnalysisDetail
import com.team.prezel.core.model.presentation.WordAnalysisStatus
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.accuracydetail.AccuracyDetailTab

@Composable
internal fun ScriptDetailList(
    selectedTab: AccuracyDetailTab,
    selectedWord: WordAnalysisDetail?,
    wordDetails: List<WordAnalysisDetail>,
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
        if (wordDetails.isNotEmpty()) {
            wordDetails.forEach { detail ->
                WordDetailCard(
                    detail = detail,
                    highlighted = detail == selectedWord,
                    useStatusTextColor = detail.usesStatusTextColor(selectedTab),
                    showStatusChip = detail.showsStatusChip(selectedTab),
                )
            }
        } else {
            EmptyDetailText(text = stringResource(R.string.feature_report_impl_script_detail_empty_speech))
        }
    }
}

private fun WordAnalysisDetail.usesStatusTextColor(selectedTab: AccuracyDetailTab): Boolean =
    when (selectedTab) {
        AccuracyDetailTab.SPEECH -> isSpeechAccuracySheetIssue
        AccuracyDetailTab.SCRIPT_MATCH -> isScriptMatchIssue
    }

private fun WordAnalysisDetail.showsStatusChip(selectedTab: AccuracyDetailTab): Boolean =
    when (selectedTab) {
        AccuracyDetailTab.SPEECH -> isSpeechAccuracySheetIssue
        AccuracyDetailTab.SCRIPT_MATCH -> isScriptMatchIssue
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
            selectedWord = PreviewWordDetail,
            wordDetails = PreviewWordDetails,
        )
    }
}

@BasicPreview
@Composable
private fun ScriptDetailListEmptyPreview() {
    PrezelTheme {
        ScriptDetailList(
            selectedTab = AccuracyDetailTab.SPEECH,
            selectedWord = null,
            wordDetails = emptyList(),
        )
    }
}

private val PreviewWordDetail = WordAnalysisDetail(
    word = "내가",
    status = WordAnalysisStatus.EXCELLENT,
    description = "매우 또렷하고 훌륭한 발음",
    accuracy = 98.0,
    startTimeMs = 1_490L,
    endTimeMs = 1_980L,
)

private val PreviewWordDetails = listOf(
    PreviewWordDetail,
    WordAnalysisDetail(
        word = "그린",
        status = WordAnalysisStatus.EXCELLENT,
        description = "매우 또렷하고 훌륭한 발음",
        accuracy = 98.0,
        startTimeMs = 1_990L,
        endTimeMs = 2_400L,
    ),
    WordAnalysisDetail(
        word = "기린",
        status = WordAnalysisStatus.EXCELLENT,
        description = "매우 또렷하고 훌륭한 발음",
        accuracy = 100.0,
        startTimeMs = 2_410L,
        endTimeMs = 2_820L,
    ),
)
