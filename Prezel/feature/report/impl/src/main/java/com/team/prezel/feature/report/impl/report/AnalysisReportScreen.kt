package com.team.prezel.feature.report.impl.report

import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.report.component.ReportBodyContent
import com.team.prezel.feature.report.impl.report.component.ReportHeaderContent
import com.team.prezel.feature.report.impl.report.component.ReportScreenLayout
import com.team.prezel.feature.report.impl.report.component.modal.ReportDialog
import com.team.prezel.feature.report.impl.report.contract.AnalysisReportUiEffect
import com.team.prezel.feature.report.impl.report.contract.AnalysisReportUiIntent
import com.team.prezel.feature.report.impl.report.contract.AnalysisReportUiState
import com.team.prezel.feature.report.impl.report.model.AnalysisReportUiMessage
import com.team.prezel.feature.report.impl.report.preview.ReportPreviewPastUiState
import com.team.prezel.feature.report.impl.report.preview.ReportPreviewUpcomingUiState

@Composable
internal fun AnalysisReportScreen(
    onBack: () -> Unit,
    navigateToAnalysisScript: (presentationId: Long, isPast: Boolean) -> Unit,
    navigateToAnalysisRecording: (presentationId: Long, isPast: Boolean) -> Unit,
    navigateToScriptAnalysis: (analysisResultId: Long) -> Unit,
    navigateToSelfFeedbackWrite: (presentationId: Long, title: String, isPast: Boolean) -> Unit,
    navigateToSpeechAccuracy: (analysisResultId: Long) -> Unit,
    navigateToScriptMatch: (analysisResultId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AnalysisReportViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current
    val resources = LocalResources.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(AnalysisReportUiIntent.FetchData)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                AnalysisReportUiEffect.NavigateToBack -> onBack()
                is AnalysisReportUiEffect.ShowMessage -> {
                    snackbarHostState.showPrezelSnackbar(
                        message = resources.getString(effect.message.resId),
                        useRaisedPosition = false,
                    )
                }

                is AnalysisReportUiEffect.NavigateToAnalysisScript -> navigateToAnalysisScript(effect.presentationId, effect.isPast)
                is AnalysisReportUiEffect.NavigateToAnalysisRecording -> navigateToAnalysisRecording(effect.presentationId, effect.isPast)
                is AnalysisReportUiEffect.NavigateToScriptAnalysis -> navigateToScriptAnalysis(effect.analysisResultId)
                is AnalysisReportUiEffect.NavigateToSelfFeedbackWrite -> {
                    navigateToSelfFeedbackWrite(effect.presentationId, effect.title, effect.isPast)
                }
                is AnalysisReportUiEffect.NavigateToSpeechAccuracy -> navigateToSpeechAccuracy(effect.analysisResultId)
                is AnalysisReportUiEffect.NavigateToScriptMatch -> navigateToScriptMatch(effect.analysisResultId)
            }
        }
    }

    AnalysisReportScreen(
        uiState = uiState,
        onBackClick = onBack,
        onDeleteClick = { viewModel.onIntent(AnalysisReportUiIntent.ClickDelete) },
        onImprovementCardIndexChange = { index -> viewModel.onIntent(AnalysisReportUiIntent.ClickGrowthGraphItem(index)) },
        onDialogDismiss = { viewModel.onIntent(AnalysisReportUiIntent.DismissDialog) },
        onDialogConfirmClick = { viewModel.onIntent(AnalysisReportUiIntent.ClickDialogConform) },
        onReWriteScriptClick = { viewModel.onIntent(AnalysisReportUiIntent.ClickReWriteScript) },
        onReRecordingClick = { viewModel.onIntent(AnalysisReportUiIntent.ClickReRecording) },
        onFeedBackWriteClick = { viewModel.onIntent(AnalysisReportUiIntent.ClickFeedbackWrite) },
        onScriptAnalysisClick = { viewModel.onIntent(AnalysisReportUiIntent.ClickScriptAnalysis) },
        onSpeechAccuracyClick = { viewModel.onIntent(AnalysisReportUiIntent.ClickSpeechAccuracy) },
        onScriptMatchClick = { viewModel.onIntent(AnalysisReportUiIntent.ClickScriptMatch) },
        modifier = modifier,
    )
}

@Composable
internal fun AnalysisReportScreen(
    uiState: AnalysisReportUiState,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onImprovementCardIndexChange: (index: Int) -> Unit,
    onDialogDismiss: () -> Unit,
    onDialogConfirmClick: () -> Unit,
    onReWriteScriptClick: () -> Unit,
    onReRecordingClick: () -> Unit,
    onFeedBackWriteClick: () -> Unit,
    onScriptAnalysisClick: () -> Unit,
    onSpeechAccuracyClick: () -> Unit,
    onScriptMatchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is AnalysisReportUiState.Content -> {
            AnalysisReportScreenContent(
                uiState = uiState,
                onBackClick = onBackClick,
                onDeleteClick = onDeleteClick,
                modifier = modifier,
                onImprovementCardIndexChange = onImprovementCardIndexChange,
                onDialogDismiss = onDialogDismiss,
                onDialogConfirmClick = onDialogConfirmClick,
                onReWriteScriptClick = onReWriteScriptClick,
                onReRecordingClick = onReRecordingClick,
                onFeedBackWriteClick = onFeedBackWriteClick,
                onScriptAnalysisClick = onScriptAnalysisClick,
                onSpeechAccuracyClick = onSpeechAccuracyClick,
                onScriptMatchClick = onScriptMatchClick,
            )
        }

        AnalysisReportUiState.Loading -> Unit
    }
}

@Composable
private fun AnalysisReportScreenContent(
    uiState: AnalysisReportUiState.Content,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onImprovementCardIndexChange: (index: Int) -> Unit,
    onDialogDismiss: () -> Unit,
    onDialogConfirmClick: () -> Unit,
    onReWriteScriptClick: () -> Unit,
    onReRecordingClick: () -> Unit,
    onFeedBackWriteClick: () -> Unit,
    onScriptAnalysisClick: () -> Unit,
    onSpeechAccuracyClick: () -> Unit,
    onScriptMatchClick: () -> Unit,
    modifier: Modifier,
) {
    uiState.reportDialog?.let { type ->
        ReportDialog(
            type = type,
            onDismissClick = onDialogDismiss,
            onConformClick = onDialogConfirmClick,
        )
    }

    ReportScreenLayout(
        appBarTitle = uiState.presentationInfo.title,
        leadingIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(PrezelIcons.ArrowLeft),
                    contentDescription = stringResource(R.string.feature_report_impl_back),
                    tint = PrezelTheme.colors.iconRegular,
                )
            }
        },
        headerContent = { titleModifier ->
            ReportHeaderContent(
                info = uiState.presentationInfo,
                titleModifier = titleModifier,
            )
        },
        bodyContent = {
            ReportBodyContent(
                uiState = uiState,
                onDeleteClick = onDeleteClick,
                onImprovementCardIndexChange = onImprovementCardIndexChange,
                onReWriteScriptClick = onReWriteScriptClick,
                onReRecordingClick = onReRecordingClick,
                onFeedBackWriteClick = onFeedBackWriteClick,
                onSpeechAccuracyClick = onSpeechAccuracyClick,
                onScriptMatchClick = onScriptMatchClick,
                onScriptAnalysisClick = onScriptAnalysisClick,
            )
        },
        modifier = modifier,
    )
}

private val AnalysisReportUiMessage.resId: Int
    @StringRes get() = when (this) {
        AnalysisReportUiMessage.FETCH_REPORT_FAILED -> R.string.feature_report_impl_fetch_report_failed
        AnalysisReportUiMessage.DELETE_REPORT_FAILED -> R.string.feature_report_impl_delete_report_failed
        AnalysisReportUiMessage.SCRIPT_MATCH_ANALYSIS_UNAVAILABLE -> {
            R.string.feature_report_impl_script_match_analysis_unavailable
        }
        AnalysisReportUiMessage.SPEECH_ANALYSIS_UNAVAILABLE -> {
            R.string.feature_report_impl_speech_analysis_unavailable
        }
    }

@BasicPreview
@Composable
private fun UpcomingAnalysisReportScreenPreview() {
    PrezelTheme {
        AnalysisReportScreen(
            uiState = ReportPreviewUpcomingUiState,
            onBackClick = { },
            onDeleteClick = { },
            onImprovementCardIndexChange = {},
            onDialogDismiss = {},
            onDialogConfirmClick = {},
            onReWriteScriptClick = {},
            onReRecordingClick = {},
            onFeedBackWriteClick = {},
            onScriptAnalysisClick = {},
            onSpeechAccuracyClick = {},
            onScriptMatchClick = {},
        )
    }
}

@BasicPreview
@Composable
private fun PastAnalysisReportScreenPreview() {
    PrezelTheme {
        AnalysisReportScreen(
            uiState = ReportPreviewPastUiState,
            onBackClick = { },
            onDeleteClick = { },
            onImprovementCardIndexChange = {},
            onDialogDismiss = {},
            onDialogConfirmClick = {},
            onReWriteScriptClick = { },
            onReRecordingClick = {},
            onFeedBackWriteClick = {},
            onScriptAnalysisClick = {},
            onSpeechAccuracyClick = {},
            onScriptMatchClick = {},
        )
    }
}

@BasicPreview
@Composable
private fun AnalysisReportScreenLoadingPreview() {
    PrezelTheme {
        AnalysisReportScreen(
            uiState = AnalysisReportUiState.Loading,
            onBackClick = { },
            onDeleteClick = { },
            onImprovementCardIndexChange = {},
            onDialogDismiss = {},
            onDialogConfirmClick = {},
            onReWriteScriptClick = { },
            onReRecordingClick = {},
            onFeedBackWriteClick = {},
            onScriptAnalysisClick = {},
            onSpeechAccuracyClick = {},
            onScriptMatchClick = {},
        )
    }
}
