package com.team.prezel.feature.setting.impl.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.setting.impl.R
import com.team.prezel.feature.setting.impl.setting.component.LogoutDialog
import com.team.prezel.feature.setting.impl.setting.component.SettingActionSection
import com.team.prezel.feature.setting.impl.setting.component.SettingBodySection
import com.team.prezel.feature.setting.impl.setting.component.SettingTopAppBar
import com.team.prezel.feature.setting.impl.setting.contract.SettingUiEffect
import com.team.prezel.feature.setting.impl.setting.contract.SettingUiIntent
import com.team.prezel.feature.setting.impl.setting.contract.SettingUiState
import com.team.prezel.feature.setting.impl.setting.model.SettingUiMessage

@Composable
internal fun SettingScreen(
    navigateBack: () -> Unit,
    navigateToDeleteAccount: () -> Unit,
    navigateToSplash: () -> Unit,
    navigateToTermsOfService: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val snackbarHostState = LocalSnackbarHostState.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resource = LocalResources.current
    var shouldShowLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.onIntent(SettingUiIntent.FetchData)

        viewModel.uiEffect.collect { effect ->
            when (effect) {
                SettingUiEffect.NavigateToSplash -> {
                    shouldShowLogoutDialog = false
                    navigateToSplash()
                }

                is SettingUiEffect.ShowMessage -> {
                    val messageRes = when (effect.message) {
                        SettingUiMessage.FETCH_USER_INFO_FAILED -> R.string.feature_setting_impl_title
                        SettingUiMessage.LOGOUT_FAILED -> R.string.feature_setting_impl_logout_failed
                    }

                    snackbarHostState.showPrezelSnackbar(
                        message = resource.getString(messageRes),
                    )
                }
            }
        }
    }

    if (shouldShowLogoutDialog) {
        LogoutDialog(
            onDismiss = { shouldShowLogoutDialog = false },
            onConfirmLogout = { viewModel.onIntent(SettingUiIntent.ClickLogout) },
        )
    }

    SettingScreen(
        uiState = uiState,
        onClickBack = navigateBack,
        onClickTermsOfService = navigateToTermsOfService,
        onClickPrivacyPolicy = navigateToPrivacyPolicy,
        onClickLogout = { shouldShowLogoutDialog = true },
        onClickWithdraw = navigateToDeleteAccount,
        modifier = modifier,
    )
}

@Composable
private fun SettingScreen(
    uiState: SettingUiState,
    onClickBack: () -> Unit,
    onClickTermsOfService: () -> Unit,
    onClickPrivacyPolicy: () -> Unit,
    onClickLogout: () -> Unit,
    onClickWithdraw: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        SettingHeaderSection(onClickBack = onClickBack)
        SettingBodySection(
            uiState = uiState,
            onClickTermsOfService = onClickTermsOfService,
            onClickPrivacyPolicy = onClickPrivacyPolicy,
            modifier = Modifier.weight(1f),
        )
        SettingActionSection(
            onClickWithdraw = onClickWithdraw,
            onClickLogout = onClickLogout,
        )
    }
}

@Composable
private fun SettingHeaderSection(
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingTopAppBar(
        onBack = onClickBack,
        modifier = modifier,
    )
}

@BasicPreview
@Composable
private fun SettingScreenPreview() {
    PrezelTheme {
        SettingScreen(
            uiState = SettingUiState(
                nickname = "프레젤",
                email = "prezel@email.com",
            ),
            onClickBack = {},
            onClickTermsOfService = {},
            onClickPrivacyPolicy = {},
            onClickLogout = {},
            onClickWithdraw = {},
        )
    }
}
