package com.team.prezel.feature.history.impl

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.team.prezel.core.ui.component.StatusView
import com.team.prezel.core.ui.state.LocalSnackbarCoroutineScope
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.history.impl.contract.HistoryFailedAnalysisUiEffect
import com.team.prezel.feature.history.impl.contract.HistoryFailedAnalysisUiIntent
import com.team.prezel.feature.history.impl.contract.HistoryFailedAnalysisUiState
import com.team.prezel.feature.history.impl.model.HistoryFailedAnalysisUiType
import com.team.prezel.feature.history.impl.model.HistoryUiMessage
import kotlinx.coroutines.launch

@Composable
internal fun HistoryFailedAnalysisScreen(
    presentationId: Long,
    analysisResultId: Long?,
    errorTypeValue: String?,
    script: String?,
    audioUrl: String?,
    onClose: () -> Unit,
    navigateToHistory: () -> Unit,
    navigateToFailedVoiceRecordingRetry: (presentationId: Long, script: String?) -> Unit,
    navigateToFailedAudioUploadRetry: (presentationId: Long, script: String?) -> Unit,
    navigateToFailedAnalysisRetry: (presentationId: Long, script: String?, audioUrl: String?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryFailedAnalysisViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current
    val snackbarScope = LocalSnackbarCoroutineScope.current
    val resources = LocalResources.current
    val failureType = HistoryFailedAnalysisUiType.from(
        value = errorTypeValue,
        script = script,
        audioUrl = audioUrl,
    )

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
        failureType = failureType,
        modifier = modifier,
        onClose = onClose,
        onRetry = {
            when (failureType) {
                is HistoryFailedAnalysisUiType.VoiceRecognition -> {
                    navigateToFailedVoiceRecordingRetry(presentationId, failureType.script)
                }

                is HistoryFailedAnalysisUiType.FileRecognition -> {
                    navigateToFailedAudioUploadRetry(presentationId, failureType.script)
                }

                is HistoryFailedAnalysisUiType.Analysis -> {
                    navigateToFailedAnalysisRetry(presentationId, failureType.script, failureType.reusableAudioUrl)
                }

                is HistoryFailedAnalysisUiType.Unknown -> {
                    if (failureType.reusableAudioUrl.isNullOrBlank()) {
                        snackbarScope.launch {
                            snackbarHostState.showPrezelSnackbar(
                                message = resources.getString(R.string.feature_history_impl_retry_data_missing),
                            )
                        }
                    } else {
                        navigateToFailedAnalysisRetry(presentationId, failureType.script, failureType.reusableAudioUrl)
                    }
                }
            }
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
    failureType: HistoryFailedAnalysisUiType,
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
            failureType = failureType,
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
    failureType: HistoryFailedAnalysisUiType,
    modifier: Modifier = Modifier,
) {
    StatusView(
        title = stringResource(failureType.titleResId),
        description = stringResource(failureType.descriptionResId),
        modifier = modifier,
        visual = {
            Image(
                painter = painterResource(failureType.visualResId),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
            )
        },
    )
}

private val HistoryUiMessage.resId: Int
    get() = when (this) {
        HistoryUiMessage.FETCH_DATA_FAILED -> R.string.feature_history_impl_fetch_data_failed
        HistoryUiMessage.DELETE_FAILED -> R.string.feature_history_impl_delete_failed
        HistoryUiMessage.DELETE_DATA_MISSING -> R.string.feature_history_impl_delete_data_missing
    }

@BasicPreview
@Composable
private fun HistoryFailedAnalysisScreenPreview() {
    PrezelTheme {
        HistoryFailedAnalysisScreen(
            uiState = HistoryFailedAnalysisUiState(),
            failureType = HistoryFailedAnalysisUiType.VoiceRecognition(script = "발표 대본"),
            onClose = {},
            onRetry = {},
            onClickDelete = {},
            onDismissDeleteDialog = {},
            onConfirmDelete = {},
        )
    }
}
