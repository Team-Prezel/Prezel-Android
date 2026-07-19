package com.team.prezel.feature.feedback.impl

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.feedback.dialog.PrezelDialog
import com.team.prezel.core.designsystem.component.feedback.dialog.PrezelDialogScope.ActionType
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.component.textfield.PrezelTextArea
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.core.ui.util.advancedImePadding
import com.team.prezel.feature.feedback.impl.contract.FeedbackUiEffect
import com.team.prezel.feature.feedback.impl.contract.FeedbackUiIntent
import com.team.prezel.feature.feedback.impl.contract.FeedbackUiState
import com.team.prezel.feature.feedback.impl.model.FeedbackUiMessage

@Composable
internal fun FeedbackScreen(
    title: String,
    navigateBack: () -> Unit,
    onSaveComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FeedbackViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current
    val resources = LocalResources.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                FeedbackUiEffect.NavigateBack -> navigateBack()
                FeedbackUiEffect.SaveComplete -> onSaveComplete()
                is FeedbackUiEffect.ShowMessage -> {
                    val resId = when (effect.message) {
                        FeedbackUiMessage.FETCH_FEEDBACK_FAILED ->
                            R.string.feature_feedback_impl_fetch_feedback_failed
                        FeedbackUiMessage.PRESENTATION_FORBIDDEN ->
                            R.string.feature_feedback_impl_presentation_forbidden
                        FeedbackUiMessage.PRESENTATION_NOT_FOUND ->
                            R.string.feature_feedback_impl_presentation_not_found
                        FeedbackUiMessage.SELF_FEEDBACK_ALREADY_WRITTEN ->
                            R.string.feature_feedback_impl_self_feedback_already_written
                        FeedbackUiMessage.SAVE_FAILED -> R.string.feature_feedback_impl_save_failed
                    }
                    snackbarHostState.showPrezelSnackbar(
                        message = resources.getString(resId),
                    )
                }
            }
        }
    }

    BackHandler {
        viewModel.onIntent(FeedbackUiIntent.ClickClose)
    }

    FeedbackScreen(
        title = title,
        uiState = uiState,
        onContentChanged = { content -> viewModel.onIntent(FeedbackUiIntent.ChangeContent(content)) },
        onClickClose = { viewModel.onIntent(FeedbackUiIntent.ClickClose) },
        onClickSave = { viewModel.onIntent(FeedbackUiIntent.ClickSave) },
        onDismissExitDialog = { viewModel.onIntent(FeedbackUiIntent.ClickDialogClose) },
        onConfirmExit = { viewModel.onIntent(FeedbackUiIntent.ClickDialogExit) },
        modifier = modifier,
    )
}

@Composable
private fun FeedbackScreen(
    title: String,
    uiState: FeedbackUiState,
    onContentChanged: (String) -> Unit,
    onClickClose: () -> Unit,
    onClickSave: () -> Unit,
    onDismissExitDialog: () -> Unit,
    onConfirmExit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isExitDialogVisible) {
        FeedbackExitDialog(
            onDismiss = onDismissExitDialog,
            onConfirmExit = onConfirmExit,
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        FeedbackTopAppBar(onClickClose = onClickClose)

        FeedbackContent(
            title = title,
            content = uiState.content,
            onContentChanged = onContentChanged,
            modifier = Modifier.weight(1f),
        )

        PrezelButtonArea(
            modifier = Modifier.advancedImePadding(),
            mainButton = { buttonModifier ->
                PrezelButton(
                    text = stringResource(R.string.feature_feedback_impl_save),
                    onClick = onClickSave,
                    enabled = uiState.isSaveEnabled,
                    type = ButtonType.FILLED,
                    hierarchy = ButtonHierarchy.PRIMARY,
                    modifier = buttonModifier,
                )
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeedbackTopAppBar(
    onClickClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrezelTopAppBar(
        title = stringResource(R.string.feature_feedback_impl_top_app_bar_title),
        modifier = modifier,
    ) {
        TrailingIcon(
            iconResId = PrezelIcons.Cancel,
            contentDescription = stringResource(R.string.feature_feedback_impl_close),
            onClick = onClickClose,
        )
    }
}

@Composable
private fun FeedbackContent(
    title: String,
    content: String,
    onContentChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val placeholders = stringArrayResource(R.array.feature_feedback_impl_placeholders)
    val placeholder = remember(placeholders.contentHashCode()) { placeholders.random() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            .padding(horizontal = PrezelTheme.spacing.V20)
            .padding(top = PrezelTheme.spacing.V32),
    ) {
        Text(
            text = stringResource(R.string.feature_feedback_impl_question, title),
            style = PrezelTheme.typography.title2Bold,
            color = PrezelTheme.colors.textLarge,
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))

        PrezelTextArea(
            value = content,
            onValueChange = onContentChanged,
            placeholder = placeholder,
            maxLength = 200,
            showCount = true,
        )
    }
}

@Composable
private fun FeedbackExitDialog(
    onDismiss: () -> Unit,
    onConfirmExit: () -> Unit,
) {
    PrezelDialog(
        title = stringResource(R.string.feature_feedback_impl_exit_dialog_title),
        description = stringResource(R.string.feature_feedback_impl_exit_dialog_description),
        onDismiss = onDismiss,
    ) {
        Action(label = stringResource(R.string.feature_feedback_impl_exit_dialog_cancel)) { onDismiss() }
        Action(
            label = stringResource(R.string.feature_feedback_impl_exit_dialog_confirm),
            type = ActionType.BAD,
        ) {
            onConfirmExit()
        }
    }
}

@BasicPreview
@Composable
private fun FeedbackScreenPreview() {
    PrezelTheme {
        FeedbackScreen(
            title = "제 7회 컨셉발표회",
            uiState = FeedbackUiState(),
            onContentChanged = {},
            onClickClose = {},
            onClickSave = {},
            onDismissExitDialog = {},
            onConfirmExit = {},
        )
    }
}

@BasicPreview
@Composable
private fun FeedbackExitDialogPreview() {
    PrezelTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FeedbackExitDialog(
                onDismiss = {},
                onConfirmExit = {},
            )
        }
    }
}
