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
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.PrezelHyperlinkButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.component.list.PrezelList
import com.team.prezel.core.designsystem.component.list.PrezelListSize
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.terms.impl.contract.TermsUiEffect
import com.team.prezel.feature.terms.impl.contract.TermsUiIntent
import com.team.prezel.feature.terms.impl.contract.TermsUiState
import com.team.prezel.feature.terms.impl.model.TermsAgreementUiModel
import com.team.prezel.feature.terms.impl.model.TermsUiMessage
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun TermsScreen(
    navigateBack: () -> Unit,
    navigateToTermsDetail: (String, String) -> Unit,
    navigateToProfile: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TermsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources = LocalResources.current
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(TermsUiIntent.FetchTerms)

        viewModel.uiEffect.collect { effect ->
            when (effect) {
                TermsUiEffect.NavigateToProfile -> navigateToProfile()

                is TermsUiEffect.ShowMessage -> {
                    val resId = when (effect.message) {
                        TermsUiMessage.FETCH_TERMS_FAILED_NETWORK -> R.string.feature_terms_impl_fetch_terms_failed_network
                        TermsUiMessage.FETCH_TERMS_FAILED_SERVER -> R.string.feature_terms_impl_fetch_terms_failed_server
                        TermsUiMessage.FETCH_TERMS_FAILED_UNKNOWN -> R.string.feature_terms_impl_fetch_terms_failed_unknown
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

    TermsScreenScreen(
        uiState = uiState,
        onBack = navigateBack,
        onToggleAll = { viewModel.onIntent(TermsUiIntent.ToggleAll) },
        onToggleTerm = { termsId -> viewModel.onIntent(TermsUiIntent.ToggleTerm(termsId)) },
        onClickTermsDetail = navigateToTermsDetail,
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
    onToggleTerm: (Long) -> Unit,
    onClickTermsDetail: (String, String) -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        TermsScreenTopAppBar(onBack = onBack)

        TermsAgreementContent(
            modifier = Modifier.weight(1f),
            uiState = uiState,
            onToggleAll = onToggleAll,
            onToggleTerm = onToggleTerm,
            onClickTermsDetail = onClickTermsDetail,
        )

        PrezelButtonArea(
            mainButton = { buttonModifier ->
                PrezelButton(
                    modifier = buttonModifier,
                    text = stringResource(R.string.feature_terms_impl_terms_continue_button_text),
                    onClick = onContinue,
                    enabled = uiState.isRequiredChecked && !uiState.isLoading && !uiState.isTermsLoading,
                    type = ButtonType.FILLED,
                    hierarchy = ButtonHierarchy.PRIMARY,
                )
            },
        )
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
    onToggleTerm: (Long) -> Unit,
    onClickTermsDetail: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(
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
            uiState.terms.forEach { term ->
                TermsAgreementRow(
                    term = term,
                    onCheckedChange = { onToggleTerm(term.termsId) },
                    onClickDetail = { onClickTermsDetail(term.titleWithRequirement(), term.contentUrl) },
                )
            }
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
    term: TermsAgreementUiModel,
    onCheckedChange: (Boolean) -> Unit,
    onClickDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrezelAccordion(
        modifier = modifier,
        title = term.titleWithRequirement(),
        nested = true,
        leadingContent = {
            PrezelCheckbox(
                checked = term.isChecked,
                size = CheckboxSize.REGULAR,
                onCheckedChange = onCheckedChange,
                extraTouchPadding = PaddingValues(),
            )
        },
        trailingContent = {
            if (term.contentUrl.isNotBlank()) {
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
                text = term.summary,
                style = PrezelTheme.typography.caption2Regular,
                color = PrezelTheme.colors.textLarge,
            )
        }
    }
}

private fun TermsAgreementUiModel.titleWithRequirement(): String =
    buildString {
        append(if (isRequired) "(필수) " else "(선택) ")
        append(title)
    }

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TermsScreenPreview() {
    var uiState by remember {
        mutableStateOf(
            TermsUiState(
                terms = persistentListOf(
                    TermsAgreementUiModel(
                        termsId = 6L,
                        title = "이용약관",
                        summary = "서비스 이용과 관련한 기본적인 권리·의무 및 책임사항을 규정합니다.",
                        contentUrl = "https://example.com/terms",
                        isRequired = true,
                    ),
                    TermsAgreementUiModel(
                        termsId = 7L,
                        title = "개인정보처리방침",
                        summary = "서비스 제공을 위한 음성 녹음 및 분석 등 개인정보 수집·이용 안내입니다.",
                        contentUrl = "https://example.com/privacy",
                        isRequired = true,
                    ),
                ),
            ),
        )
    }

    PrezelTheme {
        TermsScreenScreen(
            uiState = uiState,
            onBack = {},
            onToggleAll = {
                val next = !uiState.isAllChecked
                uiState = uiState.copy(
                    terms = uiState.terms.map { term -> term.copy(isChecked = next) }.toImmutableList(),
                )
            },
            onToggleTerm = { termsId ->
                uiState = uiState.copy(
                    terms = uiState.terms
                        .map { term ->
                            if (term.termsId == termsId) term.copy(isChecked = !term.isChecked) else term
                        }.toImmutableList(),
                )
            },
            onClickTermsDetail = { _, _ -> },
            onContinue = {},
        )
    }
}
