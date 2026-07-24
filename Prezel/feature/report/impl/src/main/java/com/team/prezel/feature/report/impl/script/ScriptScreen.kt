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
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.ScriptErrorType
import com.team.prezel.core.ui.component.HideOnScrollTopBarLayout
import com.team.prezel.core.ui.state.LocalSnackbarHostState
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
import com.team.prezel.feature.report.impl.script.model.ScriptUiMessage
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ScriptScreen(
    onClose: () -> Unit,
    viewModel: ScriptViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboard.current
    val snackbarHostState = LocalSnackbarHostState.current
    val resources = LocalResources.current

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                ScriptUiEffect.NavigateToBack -> onClose()
                is ScriptUiEffect.ShowMessage -> {
                    snackbarHostState.showPrezelSnackbar(
                        message = resources.getString(effect.message.resId),
                        useRaisedPosition = false,
                    )
                }

                is ScriptUiEffect.CurrentScriptCopyToClipBoard -> {
                    val clipData = ClipData.newPlainText("대본 교정 결과", effect.script)
                    clipboardManager.setClipEntry(ClipEntry(clipData))
                }
            }
        }
    }

    ScriptScreen(
        uiState = uiState,
        onClose = { viewModel.onIntent(ScriptUiIntent.ClickClose) },
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
    ScriptScreenContent(
        uiState = uiState,
        onClose = onClose,
        onClickCopy = onClickCopy,
        onClickCorrection = onClickCorrection,
        onApplyAllCorrections = onApplyAllCorrections,
        modifier = modifier,
    )

    ScriptCorrectionPopup(
        uiState = uiState,
        onDismissCorrectionPopup = onDismissCorrectionPopup,
        onApplyCorrection = onApplyCorrection,
    )
}

private val ScriptUiMessage.resId: Int
    get() = when (this) {
        ScriptUiMessage.FETCH_SCRIPT_DETAIL_FAILED -> R.string.feature_report_impl_fetch_script_detail_failed
        ScriptUiMessage.CORRECT_SCRIPT_FAILED -> R.string.feature_report_impl_correct_script_failed
    }

@Composable
private fun ScriptScreenContent(
    uiState: ScriptUiState,
    onClose: () -> Unit,
    onClickCopy: () -> Unit,
    onClickCorrection: (correctionId: Long, popupY: Int) -> Unit,
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
                isCorrectionClickable = !uiState.isSubmittingCorrection,
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
                isCopyEnabled = !uiState.isSubmittingCorrection,
                onCopyClick = onClickCopy,
                onApplyAllClick = onApplyAllCorrections,
            )
        },
    )
}

@Composable
private fun ScriptCorrectionPopup(
    uiState: ScriptUiState,
    onDismissCorrectionPopup: () -> Unit,
    onApplyCorrection: (correctionId: Long) -> Unit,
) {
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
                isApplyEnabled = !uiState.isSubmittingCorrection,
                onDismiss = onDismissCorrectionPopup,
                onApplyCorrection = { onApplyCorrection(correction.id) },
            )
        }
    }
}

@BasicPreview
@Composable
private fun ScriptScreenPreview() {
    val originalScript =
        """
        안녕하세요 자신의 말로 자신있게 세상을 설득할 수 있는 날을 기달리는 팀 손가락입니다.
        저희 팀은 고승환, 박하영, 조민경, 최수빈, 한효주 총 5명으로 구성되어 있으며, 다음과 같은 목차로 발표 진행하겠습니다.
        한 번쯤 발표하면서 긴장하신 경험 있으시죠.
        오늘도 다들 긴장돼는 마음으로 오셨을 것 같습니다.
        저희는 학교에서의 간단한 자기소개부터 회사의 성과보고까지 정말 다양하게, 그리고 정말 자주 발표를 경험합니다. 하지만 많은 발표를 해왔음에도 불구하고 발표를 생각했을 때 긴장하게 되는데요, 이처럼 발표를 앞둔 상황에서 경험하는 심리적 부담감을 발표 불안이라 합니다.
        면접에서도, 학교에서도, 발표 능력을 기본 역량처럼 여기는 사회 분위기로 인해 이런 불안이 더해지고자 때문이었습니다.
        가장 발표를 자주 경험하는 직장인을 예시로 들었을 때, “실수”에 대한 두려움을 발표 불안의 주된 원인으로 꼽았습니다.
        즉, 발표와 가까운 환경에 사람들 조차 실수가 두려워 발표에 어려움을 겪고 있다는 말인데요, 이 때 사람들은 클래스 수강, 집단 상담 등 발표 불안을 이겨내기 위해 정말 다양한 시도를 하고 있었습니다.
        특히 발표 코칭 학원을 등록하며 적극적인 대처를 취하는 사람들까지는 증가하고 있습니다.
        그에 따라 개개인에게 가장 알맞은 발표 수업을 제공하며 발표 코칭 시장도 슬슬 자리를 잡고 있는데요, 그러나 대학생과 사회 초년생의 평균 수입과 비교했을 때 비싼 비용과 시간적 여유가 없어 지속적으로 수강하기 어렵다는 문제점이 있었습니다.
        비용과 시간, 이런 고질적인 문제를 해결할 방법은 없을까요?
        """.trimIndent()

    PrezelTheme {
        ScriptScreen(
            uiState = ScriptUiState(
                originalScript = originalScript,
                currentScript = originalScript,
                scriptDetails = persistentListOf(
                    ScriptCorrectionUiModel(
                        id = 0L,
                        errorType = ScriptErrorType.SPELLING,
                        sentence = "안녕하세요 자신의 말로 자신있게 세상을 설득할 수 있는 날을 기달리는 팀 손가락입니다.",
                        originalText = "자신있게",
                        correctedText = "자신 있게",
                        reason = "보조 형용사 '있다'는 앞말과 띄어 쓰는 것이 자연스러워요.",
                        originalRange = 13 until 17,
                    ),
                    ScriptCorrectionUiModel(
                        id = 1L,
                        errorType = ScriptErrorType.SPELLING,
                        sentence = "안녕하세요 자신의 말로 자신있게 세상을 설득할 수 있는 날을 기달리는 팀 손가락입니다.",
                        originalText = "기달리는",
                        correctedText = "기다리는",
                        reason = "표준어는 '기다리다'를 활용한 표현이에요.",
                        originalRange = 34 until 38,
                    ),
                    ScriptCorrectionUiModel(
                        id = 2L,
                        errorType = ScriptErrorType.GRAMMAR,
                        sentence = "오늘도 다들 긴장돼는 마음으로 오셨을 것 같습니다.",
                        originalText = "긴장돼는",
                        correctedText = "긴장되는",
                        reason = "피동 표현은 '되다' 활용에 맞게 '긴장되는'으로 쓰는 편이 자연스러워요.",
                        originalRange = 151 until 155,
                    ),
                    ScriptCorrectionUiModel(
                        id = 3L,
                        errorType = ScriptErrorType.GRAMMAR,
                        sentence = "면접에서도, 학교에서도, 발표 능력을 기본 역량처럼 여기는 사회 분위기로 인해 이런 불안이 더해지고자 때문이었습니다.",
                        originalText = "더해지고자",
                        correctedText = "더해졌기",
                        reason = "원인 설명 문맥에서는 의도 표현보다 완료된 상태를 나타내는 표현이 자연스러워요.",
                        originalRange = 373 until 378,
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
