package com.team.prezel.feature.badge.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.badge.api.BadgeNavKey
import com.team.prezel.feature.badge.impl.R
import com.team.prezel.feature.badge.impl.component.BadgeDetailScreenContent
import com.team.prezel.feature.badge.impl.component.badgeScreenPreviewState
import com.team.prezel.feature.badge.impl.contract.BadgeUiEffect
import com.team.prezel.feature.badge.impl.contract.BadgeUiIntent
import com.team.prezel.feature.badge.impl.contract.BadgeUiState
import com.team.prezel.feature.badge.impl.model.BadgeUiMessage
import kotlinx.coroutines.launch

@Composable
internal fun BadgeScreen(
    badgeNavKey: BadgeNavKey,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BadgeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current
    val coroutineScope = rememberCoroutineScope()
    val fetchBadgeDetailFailedMessage = stringResource(R.string.feature_badge_impl_message_fetch_badge_detail_failed)
    val badgeImageLoadFailedMessage = stringResource(R.string.feature_badge_impl_message_badge_image_load_failed)

    LaunchedEffect(viewModel, badgeNavKey.badgeCode) {
        viewModel.onIntent(BadgeUiIntent.FetchBadgeDetail(badgeCode = badgeNavKey.badgeCode))
    }

    LaunchedEffect(viewModel, fetchBadgeDetailFailedMessage) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is BadgeUiEffect.ShowMessage -> {
                    val message = when (effect.message) {
                        BadgeUiMessage.FETCH_BADGE_DETAIL_FAILED -> fetchBadgeDetailFailedMessage
                    }
                    snackbarHostState.showPrezelSnackbar(message = message)
                }
            }
        }
    }

    BadgeScreenScreen(
        uiState = uiState,
        onBack = onBack,
        onBadgeImageLoadFailure = {
            coroutineScope.launch {
                snackbarHostState.showPrezelSnackbar(message = badgeImageLoadFailedMessage)
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun BadgeScreenScreen(
    uiState: BadgeUiState,
    onBack: () -> Unit,
    onBadgeImageLoadFailure: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        BadgeDetailScreenContent(
            badgeDetail = uiState.badgeDetail,
            onBack = onBack,
            onImageLoadFailure = onBadgeImageLoadFailure,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@BasicPreview
@Composable
private fun BadgeScreenPreview() {
    PrezelTheme {
        BadgeScreenScreen(
            uiState = badgeScreenPreviewState(),
            onBack = {},
            onBadgeImageLoadFailure = {},
        )
    }
}
