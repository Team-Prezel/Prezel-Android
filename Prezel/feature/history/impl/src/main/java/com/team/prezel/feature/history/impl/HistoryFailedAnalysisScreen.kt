package com.team.prezel.feature.history.impl

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.feedback.dialog.PrezelDialog
import com.team.prezel.core.designsystem.component.feedback.dialog.PrezelDialogScope
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.PresentationAnalysisErrorType
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.history.impl.contract.HistoryFailedAnalysisUiEffect
import com.team.prezel.feature.history.impl.contract.HistoryFailedAnalysisUiIntent
import com.team.prezel.feature.history.impl.contract.HistoryFailedAnalysisUiState
import com.team.prezel.feature.history.impl.model.HistoryUiMessage
import com.team.prezel.core.ui.R as CoreUiR

@Composable
internal fun HistoryFailedAnalysisScreen(
    presentationId: Long,
    analysisResultId: Long?,
    errorTypeValue: String?,
    script: String?,
    audioUrl: String?,
    onClose: () -> Unit,
    navigateToHistory: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryFailedAnalysisViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current
    val resources = LocalResources.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                HistoryFailedAnalysisUiEffect.NavigateToHistory -> navigateToHistory()
                is HistoryFailedAnalysisUiEffect.ShowMessage -> {
                    snackbarHostState.showPrezelSnackbar(message = resources.getString(effect.message.resId))
                }
            }
        }
    }

    HistoryFailedAnalysisScreen(
        uiState = uiState,
        errorType = PresentationAnalysisErrorType.from(errorTypeValue) ?: PresentationAnalysisErrorType.ANALYSIS,
        modifier = modifier,
        onClose = onClose,
        onRetry = {
            viewModel.onIntent(
                HistoryFailedAnalysisUiIntent.Retry(
                    presentationId = presentationId,
                    script = script,
                    audioUrl = audioUrl,
                ),
            )
        },
        onClickDelete = { viewModel.onIntent(HistoryFailedAnalysisUiIntent.ClickDelete) },
        onDismissDeleteDialog = { viewModel.onIntent(HistoryFailedAnalysisUiIntent.DismissDeleteDialog) },
        onConfirmDelete = {
            viewModel.onIntent(HistoryFailedAnalysisUiIntent.ConfirmDelete(analysisResultId = analysisResultId))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryFailedAnalysisScreen(
    uiState: HistoryFailedAnalysisUiState,
    errorType: PresentationAnalysisErrorType,
    onClose: () -> Unit,
    onRetry: () -> Unit,
    onClickDelete: () -> Unit,
    onDismissDeleteDialog: () -> Unit,
    onConfirmDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        PrezelTopAppBar(title = stringResource(R.string.feature_history_impl_failed_analysis_title)) {
            TrailingIcon(
                iconResId = PrezelIcons.Cancel,
                contentDescription = stringResource(R.string.feature_history_impl_close),
                onClick = onClose,
            )
        }

        HistoryFailedAnalysisContent(
            title = stringResource(errorType.titleResId),
            description = stringResource(errorType.descriptionResId),
            visualResId = errorType.visualResId,
            modifier = Modifier.weight(1f),
        )

        PrezelButtonArea(
            isVertical = false,
            mainButton = { buttonModifier ->
                PrezelButton(
                    text = stringResource(R.string.feature_history_impl_retry),
                    modifier = buttonModifier,
                    type = ButtonType.FILLED,
                    hierarchy = ButtonHierarchy.SECONDARY,
                    enabled = !uiState.isLoading,
                    onClick = onRetry,
                )
            },
            subButton = { buttonModifier ->
                PrezelButton(
                    text = stringResource(R.string.feature_history_impl_delete),
                    modifier = buttonModifier,
                    type = ButtonType.OUTLINED,
                    hierarchy = ButtonHierarchy.SECONDARY,
                    enabled = !uiState.isLoading,
                    onClick = onClickDelete,
                )
            },
        )
    }

    if (uiState.showDeleteDialog) {
        PrezelDialog(
            title = stringResource(R.string.feature_history_impl_delete_report_dialog_title),
            description = stringResource(R.string.feature_history_impl_delete_report_dialog_description),
            onDismiss = onDismissDeleteDialog,
        ) {
            Action(
                label = stringResource(R.string.feature_history_impl_dialog_cancel),
                onClick = onDismissDeleteDialog,
            )
            Action(
                label = stringResource(R.string.feature_history_impl_delete),
                type = PrezelDialogScope.ActionType.BAD,
                onClick = onConfirmDelete,
            )
        }
    }
}

@Composable
private fun HistoryFailedAnalysisContent(
    title: String,
    description: String,
    @DrawableRes visualResId: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(visualResId),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
        )
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))
        Text(
            text = title,
            style = PrezelTheme.typography.body2Bold,
            color = PrezelTheme.colors.textLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V6))
        Text(
            text = description,
            style = PrezelTheme.typography.caption1Regular,
            color = PrezelTheme.colors.textMedium,
            textAlign = TextAlign.Center,
        )
    }
}

private val PresentationAnalysisErrorType.titleResId: Int
    @StringRes get() = when (this) {
        PresentationAnalysisErrorType.VOICE_RECOGNITION -> R.string.feature_history_impl_voice_recognition_failed_title
        PresentationAnalysisErrorType.FILE_RECOGNITION -> R.string.feature_history_impl_file_recognition_failed_title
        PresentationAnalysisErrorType.ANALYSIS -> R.string.feature_history_impl_analysis_failed_title
    }

private val PresentationAnalysisErrorType.descriptionResId: Int
    @StringRes get() = when (this) {
        PresentationAnalysisErrorType.VOICE_RECOGNITION -> R.string.feature_history_impl_voice_recognition_failed_description
        PresentationAnalysisErrorType.FILE_RECOGNITION -> R.string.feature_history_impl_file_recognition_failed_description
        PresentationAnalysisErrorType.ANALYSIS -> R.string.feature_history_impl_analysis_failed_description
    }

private val PresentationAnalysisErrorType.visualResId: Int
    @DrawableRes get() = when (this) {
        PresentationAnalysisErrorType.VOICE_RECOGNITION,
        PresentationAnalysisErrorType.FILE_RECOGNITION,
        -> CoreUiR.drawable.core_ui_error_voice

        PresentationAnalysisErrorType.ANALYSIS -> CoreUiR.drawable.core_ui_error_analyze
    }

private val HistoryUiMessage.resId: Int
    get() = when (this) {
        HistoryUiMessage.FETCH_DATA_FAILED -> R.string.feature_history_impl_fetch_data_failed
        HistoryUiMessage.RETRY_FAILED -> R.string.feature_history_impl_retry_failed
        HistoryUiMessage.DELETE_FAILED -> R.string.feature_history_impl_delete_failed
        HistoryUiMessage.RETRY_DATA_MISSING -> R.string.feature_history_impl_retry_data_missing
        HistoryUiMessage.DELETE_DATA_MISSING -> R.string.feature_history_impl_delete_data_missing
    }

@BasicPreview
@Composable
private fun HistoryFailedAnalysisScreenPreview() {
    PrezelTheme {
        HistoryFailedAnalysisScreen(
            uiState = HistoryFailedAnalysisUiState(),
            errorType = PresentationAnalysisErrorType.VOICE_RECOGNITION,
            onClose = {},
            onRetry = {},
            onClickDelete = {},
            onDismissDeleteDialog = {},
            onConfirmDelete = {},
        )
    }
}
