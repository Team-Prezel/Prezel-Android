package com.team.prezel.feature.setting.impl.delete

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.PrezelCheckbox
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.core.ui.util.noRippleClickable
import com.team.prezel.feature.setting.impl.R
import com.team.prezel.feature.setting.impl.delete.component.DeleteAccountActionSection
import com.team.prezel.feature.setting.impl.delete.component.DeleteAccountConfirmDialog
import com.team.prezel.feature.setting.impl.delete.component.DeleteAccountNoticeStep
import com.team.prezel.feature.setting.impl.delete.component.DeleteAccountReasonStep
import com.team.prezel.feature.setting.impl.delete.component.DeleteAccountTopAppBar
import com.team.prezel.feature.setting.impl.delete.contract.DeleteAccountUiEffect
import com.team.prezel.feature.setting.impl.delete.contract.DeleteAccountUiIntent
import com.team.prezel.feature.setting.impl.delete.contract.DeleteAccountUiState
import com.team.prezel.feature.setting.impl.delete.model.DeleteAccountReasonOption
import com.team.prezel.feature.setting.impl.delete.model.DeleteAccountStep
import com.team.prezel.feature.setting.impl.delete.model.DeleteAccountUiMessage

@Composable
internal fun DeleteAccountScreen(
    navigateBack: () -> Unit,
    navigateToSplash: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DeleteAccountViewModel = hiltViewModel(),
) {
    val snackbarHostState = LocalSnackbarHostState.current
    val resources = LocalResources.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                DeleteAccountUiEffect.NavigateToSplash -> {
                    navigateToSplash()
                    snackbarHostState.showPrezelSnackbar(message = resources.getString(R.string.feature_setting_impl_delete_account_withdraw_success))
                }

                is DeleteAccountUiEffect.ShowMessage -> {
                    snackbarHostState.showPrezelSnackbar(message = resources.getString(effect.message.toMessageRes()))
                }
            }
        }
    }

    BackHandler {
        navigateBack()
    }

    DeleteAccountScreen(
        uiState = uiState,
        onClickClose = navigateBack,
        onToggleNoticeChecked = { checked ->
            viewModel.onIntent(DeleteAccountUiIntent.ToggleNoticeChecked(checked))
        },
        onClickNext = { viewModel.onIntent(DeleteAccountUiIntent.ClickNext) },
        onSelectReason = { reason -> viewModel.onIntent(DeleteAccountUiIntent.SelectReason(reason)) },
        onOtherReasonChanged = { value -> viewModel.onIntent(DeleteAccountUiIntent.ChangeOtherReason(value)) },
        onClickWithdraw = { viewModel.onIntent(DeleteAccountUiIntent.ClickWithdraw) },
        onDismissDialog = { viewModel.onIntent(DeleteAccountUiIntent.DismissDialog) },
        onConfirmWithdraw = { viewModel.onIntent(DeleteAccountUiIntent.ConfirmWithdraw) },
        modifier = modifier,
    )
}

private fun DeleteAccountUiMessage.toMessageRes(): Int =
    when (this) {
        DeleteAccountUiMessage.WITHDRAW_FAILED -> R.string.feature_setting_impl_delete_account_withdraw_failed
    }

@Composable
private fun DeleteAccountScreen(
    uiState: DeleteAccountUiState,
    onClickClose: () -> Unit,
    onToggleNoticeChecked: (Boolean) -> Unit,
    onClickNext: () -> Unit,
    onSelectReason: (DeleteAccountReasonOption) -> Unit,
    onOtherReasonChanged: (String) -> Unit,
    onClickWithdraw: () -> Unit,
    onDismissDialog: () -> Unit,
    onConfirmWithdraw: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isConfirmDialogVisible) {
        DeleteAccountConfirmDialog(
            onDismissDialog = onDismissDialog,
            onConfirmWithdraw = onConfirmWithdraw,
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        DeleteAccountTopAppBar(onClickClose = onClickClose)

        DeleteAccountContent(
            uiState = uiState,
            onToggleNoticeChecked = onToggleNoticeChecked,
            onSelectReason = onSelectReason,
            onOtherReasonChanged = onOtherReasonChanged,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            if (uiState.step == DeleteAccountStep.NOTICE) {
                DeletionAgreement(
                    isChecked = uiState.isNoticeChecked,
                    onCheckedChange = onToggleNoticeChecked,
                    modifier = Modifier.padding(horizontal = PrezelTheme.spacing.V20),
                )
            }
            DeleteAccountActionSection(
                step = uiState.step,
                enabled = uiState.isPrimaryActionEnabled,
                onClickNext = onClickNext,
                onClickWithdraw = onClickWithdraw,
            )
        }
    }
}

@Composable
private fun DeleteAccountContent(
    uiState: DeleteAccountUiState,
    onToggleNoticeChecked: (Boolean) -> Unit,
    onSelectReason: (DeleteAccountReasonOption) -> Unit,
    onOtherReasonChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (uiState.step) {
            DeleteAccountStep.NOTICE -> DeleteAccountNoticeStep(
                isChecked = uiState.isNoticeChecked,
                onCheckedChange = onToggleNoticeChecked,
            )

            DeleteAccountStep.REASON -> DeleteAccountReasonStep(
                selectedReason = uiState.selectedReason,
                otherReasonText = uiState.otherReasonText,
                onSelectReason = onSelectReason,
                onOtherReasonChanged = onOtherReasonChanged,
            )
        }
    }
}

@Composable
private fun DeletionAgreement(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { onCheckedChange(!isChecked) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PrezelCheckbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            extraTouchPadding = PaddingValues(),
        )

        Spacer(modifier = Modifier.width(PrezelTheme.spacing.V8))

        Text(
            text = stringResource(R.string.feature_setting_impl_delete_account_notice_check),
            style = PrezelTheme.typography.body3Regular,
            color = PrezelTheme.colors.textRegular,
        )
    }
}

@BasicPreview
@Composable
private fun DeleteAccountNoticeStepScreenPreview() {
    PrezelTheme {
        DeleteAccountScreen(
            uiState = DeleteAccountUiState(
                step = DeleteAccountStep.NOTICE,
            ),
            onClickClose = {},
            onToggleNoticeChecked = {},
            onClickNext = {},
            onSelectReason = {},
            onOtherReasonChanged = {},
            onClickWithdraw = {},
            onDismissDialog = {},
            onConfirmWithdraw = {},
        )
    }
}

@BasicPreview
@Composable
private fun DeleteAccountReasonStepScreenPreview() {
    PrezelTheme {
        DeleteAccountScreen(
            uiState = DeleteAccountUiState(
                step = DeleteAccountStep.REASON,
                selectedReason = DeleteAccountReasonOption.Etc,
            ),
            onClickClose = {},
            onToggleNoticeChecked = {},
            onClickNext = {},
            onSelectReason = {},
            onOtherReasonChanged = {},
            onClickWithdraw = {},
            onDismissDialog = {},
            onConfirmWithdraw = {},
        )
    }
}
