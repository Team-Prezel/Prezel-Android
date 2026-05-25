package com.team.prezel.feature.report.impl

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
import com.team.prezel.feature.report.impl.component.ReportBodyContent
import com.team.prezel.feature.report.impl.component.ReportHeaderContent
import com.team.prezel.feature.report.impl.component.ReportScreenLayout
import com.team.prezel.feature.report.impl.component.modal.ReportDialog
import com.team.prezel.feature.report.impl.contract.AnalysisReportUiEffect
import com.team.prezel.feature.report.impl.contract.AnalysisReportUiIntent
import com.team.prezel.feature.report.impl.contract.AnalysisReportUiState
import com.team.prezel.feature.report.impl.model.AnalysisReportUiMessage
import com.team.prezel.feature.report.impl.preview.ReportPreviewPastUiState
import com.team.prezel.feature.report.impl.preview.ReportPreviewUpcomingUiState

@Composable
internal fun AnalysisReportScreen(
    onBack: () -> Unit,
    navigateToAnalysisScript: (presentationId: Long) -> Unit,
    navigateToAnalysisRecording: (presentationId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AnalysisReportViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current
    val resources = LocalResources.current

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

                is AnalysisReportUiEffect.NavigateToAnalysisScript -> navigateToAnalysisScript(effect.presentationId)
                is AnalysisReportUiEffect.NavigateToAnalysisRecording -> navigateToAnalysisRecording(effect.presentationId)
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
            )
        },
        modifier = modifier,
    )
}

private val AnalysisReportUiMessage.resId: Int
    @StringRes get() = when (this) {
        AnalysisReportUiMessage.FETCH_REPORT_FAILED -> R.string.feature_report_impl_fetch_report_failed
        AnalysisReportUiMessage.DELETE_REPORT_FAILED -> R.string.feature_report_impl_delete_report_failed
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
            onReWriteScriptClick = { },
            onReRecordingClick = {},
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
        )
    }
}
