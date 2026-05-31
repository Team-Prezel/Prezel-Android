package com.team.prezel.feature.report.impl.script

import android.content.ClipData
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.HideOnScrollTopBarLayout
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.script.component.ScriptActionBar
import com.team.prezel.feature.report.impl.script.component.ScriptAppBar
import com.team.prezel.feature.report.impl.script.component.ScriptCorrectionPopup
import com.team.prezel.feature.report.impl.script.component.ScriptResultSummary
import com.team.prezel.feature.report.impl.script.component.ScriptTextContent
import com.team.prezel.feature.report.impl.script.contract.ScriptUiEffect
import com.team.prezel.feature.report.impl.script.contract.ScriptUiIntent
import com.team.prezel.feature.report.impl.script.contract.ScriptUiState
import com.team.prezel.feature.report.impl.script.model.ScriptCorrectionUiModel

@Composable
internal fun ScriptScreen(
    onClose: () -> Unit,
    viewModel: ScriptViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboard.current

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ScriptUiEffect.CurrentScriptCopyToClipBoard -> {
                    val clipData = ClipData.newPlainText("대본 교정 결과", effect.script)
                    clipboardManager.setClipEntry(ClipEntry(clipData))
                }
            }
        }
    }

    ScriptScreen(
        uiState = uiState,
        onClose = onClose,
        onClickCopy = { viewModel.onIntent(ScriptUiIntent.ClickCopy) },
        onClickCorrection = { correctionId, popupY ->
            viewModel.onIntent(ScriptUiIntent.ClickCorrection(correctionId = correctionId, popupY = popupY))
        },
        onDismissCorrectionPopup = {
            viewModel.onIntent(ScriptUiIntent.DismissCorrectionPopup)
        },
        onApplyCorrection = { correctionId ->
            viewModel.onIntent(ScriptUiIntent.ApplyCorrection(correctionId = correctionId))
        },
        onApplyAllCorrections = {
            viewModel.onIntent(ScriptUiIntent.ApplyAllCorrections)
        },
    )
}

@Composable
internal fun ScriptScreen(
    uiState: ScriptUiState,
    onClose: () -> Unit,
    onClickCopy: () -> Unit,
    onClickCorrection: (correctionId: Long, popupY: Int) -> Unit,
    onDismissCorrectionPopup: () -> Unit,
    onApplyCorrection: (correctionId: Long) -> Unit,
    onApplyAllCorrections: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HideOnScrollTopBarLayout(
        modifier = modifier,
        scrollState = rememberScrollState(),
        topBar = {
            ScriptAppBar(
                title = stringResource(R.string.feature_report_impl_section_script_analysis),
                onCloseClick = onClose,
            )
        },
        body = {
            ScriptTextContent(
                script = uiState.currentScript,
                corrections = uiState.scriptDetails,
                onClickCorrection = onClickCorrection,
                modifier = Modifier
                    .padding(horizontal = PrezelTheme.spacing.V20)
                    .padding(bottom = PrezelTheme.spacing.V16),
            )
        },
        bottomBar = {
            ScriptResultSummary(
                spellingCount = uiState.unappliedSpellingErrors,
                grammarCount = uiState.unappliedGrammarErrors,
            )

            ScriptActionBar(
                isApplyAllEnabled = uiState.enabledAllCorrectionButton,
                onCopyClick = onClickCopy,
                onApplyAllClick = onApplyAllCorrections,
            )
        },
    )

    uiState.selectedCorrection?.let { correction ->
        Popup(
            alignment = Alignment.TopStart,
            offset = IntOffset(
                x = 0,
                y = uiState.selectedCorrectionPopupY,
            ),
            onDismissRequest = onDismissCorrectionPopup,
            properties = PopupProperties(focusable = true),
        ) {
            ScriptCorrectionPopup(
                correction = correction,
                onDismiss = onDismissCorrectionPopup,
                onApplyCorrection = { onApplyCorrection(correction.id) },
            )
        }
    }
}

@BasicPreview
@Composable
private fun ScriptScreenPreview() {
    PrezelTheme {
        ScriptScreen(
            uiState = ScriptUiState(
                currentScript =
                    """
                    안녕하세요 자신의 말로 자신있게 세상을 설득할 수 있는 날을 기달리는 팀 손가락입니다.
                    오늘도 다들 긴장돼는 마음으로 오셨을 것 같습니다.
                    면접에서도, 학교에서도, 발표 능력을 기본 역량처럼 여기는 사회 분위기로 인해 이런 불안이 더해지고자 때문이었습니다.
                    """.trimIndent(),
                scriptDetails = kotlinx.collections.immutable.persistentListOf(
                    ScriptCorrectionUiModel(
                        id = 0L,
                        errorType = com.team.prezel.core.model.presentation.ScriptErrorType.SPELL,
                        sentence = "안녕하세요 자신의 말로 자신있게 세상을 설득할 수 있는 날을 기달리는 팀 손가락입니다.",
                        originalText = "기달리는",
                        correctedText = "기다리는",
                        reason = "표준어는 '기다리다'를 활용한 표현이에요.",
                        originalRange = 30 until 34,
                    ),
                    ScriptCorrectionUiModel(
                        id = 1L,
                        errorType = com.team.prezel.core.model.presentation.ScriptErrorType.GRAMMAR,
                        sentence = "오늘도 다들 긴장돼는 마음으로 오셨을 것 같습니다.",
                        originalText = "긴장돼는",
                        correctedText = "긴장되는",
                        reason = "보조 용언 활용을 바로잡으면 문장이 자연스러워져요.",
                        originalRange = 55 until 60,
                    ),
                ),
            ),
            onClose = {},
            onClickCopy = {},
            onClickCorrection = { _, _ -> },
            onDismissCorrectionPopup = {},
            onApplyCorrection = {},
            onApplyAllCorrections = {},
        )
    }
}
