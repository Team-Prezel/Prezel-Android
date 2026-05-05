package com.team.prezel.feature.terms.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.CheckboxSize
import com.team.prezel.core.designsystem.component.PrezelAccordion
import com.team.prezel.core.designsystem.component.PrezelCheckbox
import com.team.prezel.core.designsystem.component.PrezelDividerType
import com.team.prezel.core.designsystem.component.PrezelHorizontalDivider
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.actions.button.PrezelHyperlinkButton
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.component.list.PrezelList
import com.team.prezel.core.designsystem.component.list.PrezelListSize
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.terms.impl.component.TermsDetailModal
import com.team.prezel.feature.terms.impl.contract.TermsUiEffect
import com.team.prezel.feature.terms.impl.contract.TermsUiIntent
import com.team.prezel.feature.terms.impl.contract.TermsUiState
import com.team.prezel.feature.terms.impl.model.TermsUiMessage

@Composable
internal fun TermsScreen(
    navigateBack: () -> Unit,
    navigateToProfile: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TermsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources = LocalResources.current
    val snackbarHostState = LocalSnackbarHostState.current
    var activeDetailUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                TermsUiEffect.NavigateToProfile -> navigateToProfile()

                is TermsUiEffect.ShowMessage -> {
                    val resId = when (effect.message) {
                        TermsUiMessage.AGREE_TERMS_FAILED_INVALID_REQUEST -> R.string.feature_terms_impl_agree_terms_failed_invalid_request
                        TermsUiMessage.AGREE_TERMS_FAILED_NETWORK -> R.string.feature_terms_impl_agree_terms_failed_network
                        TermsUiMessage.AGREE_TERMS_FAILED_SERVER -> R.string.feature_terms_impl_agree_terms_failed_server
                        TermsUiMessage.AGREE_TERMS_FAILED_UNKNOWN -> R.string.feature_terms_impl_agree_terms_failed_unknown
                    }
                    snackbarHostState.showPrezelSnackbar(message = resources.getString(resId))
                }
            }
        }
    }

    activeDetailUrl?.let { url ->
        TermsDetailModal(
            url = url,
            onDismiss = { activeDetailUrl = null },
        )
    }

    TermsScreenScreen(
        uiState = uiState,
        onBack = navigateBack,
        onToggleAll = { viewModel.onIntent(TermsUiIntent.ToggleAll) },
        onToggleTermsOfService = { viewModel.onIntent(TermsUiIntent.ToggleTermsOfService) },
        onTogglePrivacyPolicy = { viewModel.onIntent(TermsUiIntent.TogglePrivacyPolicy) },
        onToggleMarketingConsent = { viewModel.onIntent(TermsUiIntent.ToggleMarketingConsent) },
        onClickTermsOfServiceDetail = { activeDetailUrl = BuildConfig.TERMS_OF_SERVICE_URL },
        onClickPrivacyPolicyDetail = { activeDetailUrl = BuildConfig.PRIVACY_POLICY_URL },
        onContinue = { viewModel.onIntent(TermsUiIntent.ClickContinue) },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TermsScreenScreen(
    uiState: TermsUiState,
    onBack: () -> Unit,
    onToggleAll: () -> Unit,
    onToggleTermsOfService: () -> Unit,
    onTogglePrivacyPolicy: () -> Unit,
    onToggleMarketingConsent: () -> Unit,
    onClickTermsOfServiceDetail: () -> Unit,
    onClickPrivacyPolicyDetail: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val resources = LocalResources.current

    Column(modifier = modifier.fillMaxSize()) {
        TermsScreenTopAppBar(onBack = onBack)

        TermsAgreementContent(
            modifier = Modifier.weight(1f),
            uiState = uiState,
            onToggleAll = onToggleAll,
            onToggleTermsOfService = onToggleTermsOfService,
            onClickTermsOfServiceDetail = onClickTermsOfServiceDetail,
            onTogglePrivacyPolicy = onTogglePrivacyPolicy,
            onClickPrivacyPolicyDetail = onClickPrivacyPolicyDetail,
            onToggleMarketingConsent = onToggleMarketingConsent,
        )

        PrezelButtonArea {
            MainButton(
                label = resources.getString(R.string.feature_terms_impl_terms_continue_button_text),
                enabled = uiState.isRequiredChecked && !uiState.isLoading,
                onClick = onContinue,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TermsScreenTopAppBar(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrezelTopAppBar(
        modifier = modifier,
        title = {
            Text(text = stringResource(R.string.feature_terms_impl_terms_title))
        },
        leadingIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(PrezelIcons.ArrowLeft),
                    contentDescription = stringResource(R.string.feature_terms_impl_back_icon_description),
                )
            }
        },
    )
}

@Composable
private fun TermsAgreementContent(
    uiState: TermsUiState,
    onToggleAll: () -> Unit,
    onToggleTermsOfService: () -> Unit,
    onClickTermsOfServiceDetail: () -> Unit,
    onTogglePrivacyPolicy: () -> Unit,
    onClickPrivacyPolicyDetail: () -> Unit,
    onToggleMarketingConsent: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = PrezelTheme.spacing.V20,
                vertical = PrezelTheme.spacing.V16,
            ),
    ) {
        TermsSelectAllRow(
            isAllChecked = uiState.isAllChecked,
            onToggleAll = onToggleAll,
        )

        PrezelHorizontalDivider(type = PrezelDividerType.THICK)

        TermsAgreementSection {
            TermsAgreementRow(
                checked = uiState.isTermsOfServiceChecked,
                title = stringResource(R.string.feature_terms_impl_terms_of_service),
                summary = stringResource(R.string.feature_terms_impl_terms_of_service_summary),
                isShowDetailButton = true,
                onCheckedChange = { onToggleTermsOfService() },
                onClickDetail = onClickTermsOfServiceDetail,
            )
            TermsAgreementRow(
                checked = uiState.isPrivacyPolicyChecked,
                title = stringResource(R.string.feature_terms_impl_privacy_policy),
                summary = stringResource(R.string.feature_terms_impl_privacy_policy_summary),
                isShowDetailButton = true,
                onCheckedChange = { onTogglePrivacyPolicy() },
                onClickDetail = onClickPrivacyPolicyDetail,
            )
            TermsAgreementRow(
                checked = uiState.isMarketingConsentChecked,
                title = stringResource(R.string.feature_terms_impl_marketing_consent),
                summary = stringResource(R.string.feature_terms_impl_marketing_consent_summary),
                isShowDetailButton = false,
                onCheckedChange = { onToggleMarketingConsent() },
                onClickDetail = {},
            )
        }
    }
}

@Composable
private fun TermsSelectAllRow(
    isAllChecked: Boolean,
    onToggleAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrezelList(
        modifier = modifier.clickable(
            onClick = onToggleAll,
            indication = null,
            interactionSource = null,
        ),
        title = stringResource(R.string.feature_terms_impl_terms_all),
        size = PrezelListSize.REGULAR,
        nested = true,
        leadingContent = {
            PrezelCheckbox(
                checked = isAllChecked,
                size = CheckboxSize.REGULAR,
                onCheckedChange = { onToggleAll() },
                extraTouchPadding = PaddingValues(
                    start = PrezelTheme.spacing.V8,
                    top = PrezelTheme.spacing.V8,
                    bottom = PrezelTheme.spacing.V8,
                ),
            )
        },
    )
}

@Composable
private fun TermsAgreementSection(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = PrezelTheme.spacing.V14)
            .padding(vertical = PrezelTheme.spacing.V12),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
        content = content,
    )
}

@Composable
private fun TermsAgreementRow(
    checked: Boolean,
    title: String,
    summary: String,
    isShowDetailButton: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClickDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrezelAccordion(
        modifier = modifier,
        title = title,
        nested = true,
        leadingContent = {
            PrezelCheckbox(
                checked = checked,
                size = CheckboxSize.REGULAR,
                onCheckedChange = onCheckedChange,
                extraTouchPadding = PaddingValues(),
            )
        },
        trailingContent = {
            if (isShowDetailButton) {
                PrezelHyperlinkButton(
                    text = stringResource(R.string.feature_terms_impl_terms_detail_button_text),
                    onClick = onClickDetail,
                )
            }
        },
    ) {
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrezelTheme.colors.bgMedium)
                .padding(all = PrezelTheme.spacing.V12),
        ) {
            Text(
                text = summary,
                style = PrezelTheme.typography.caption2Regular,
                color = PrezelTheme.colors.textLarge,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TermsScreenPreview() {
    var uiState by remember { mutableStateOf(TermsUiState()) }

    PrezelTheme {
        TermsScreenScreen(
            uiState = uiState,
            onBack = {},
            onToggleAll = {
                val next = !uiState.isAllChecked
                uiState = uiState.copy(
                    isTermsOfServiceChecked = next,
                    isPrivacyPolicyChecked = next,
                    isMarketingConsentChecked = next,
                )
            },
            onToggleTermsOfService = { uiState = uiState.copy(isTermsOfServiceChecked = !uiState.isTermsOfServiceChecked) },
            onTogglePrivacyPolicy = { uiState = uiState.copy(isPrivacyPolicyChecked = !uiState.isPrivacyPolicyChecked) },
            onToggleMarketingConsent = { uiState = uiState.copy(isMarketingConsentChecked = !uiState.isMarketingConsentChecked) },
            onClickTermsOfServiceDetail = {},
            onClickPrivacyPolicyDetail = {},
            onContinue = {},
        )
    }
}
