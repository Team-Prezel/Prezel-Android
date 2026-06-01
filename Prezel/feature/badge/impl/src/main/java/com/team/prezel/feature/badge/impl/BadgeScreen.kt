package com.team.prezel.feature.badge.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.badge.impl.component.BadgeDetailModal
import com.team.prezel.feature.badge.impl.component.BadgeListContent
import com.team.prezel.feature.badge.impl.component.badgeScreenPreviewState
import com.team.prezel.feature.badge.impl.contract.BadgeUiEffect
import com.team.prezel.feature.badge.impl.contract.BadgeUiIntent
import com.team.prezel.feature.badge.impl.contract.BadgeUiState
import com.team.prezel.feature.badge.impl.model.BadgeUiMessage

@Composable
internal fun BadgeScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BadgeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is BadgeUiEffect.ShowMessage -> {
                    val message = when (effect.message) {
                        BadgeUiMessage.FETCH_DATA_FAILED -> "뱃지 목록을 불러오지 못했어요."
                        BadgeUiMessage.FETCH_BADGE_DETAIL_FAILED -> "뱃지 상세 정보를 불러오지 못했어요."
                    }
                    snackbarHostState.showPrezelSnackbar(message = message)
                }
            }
        }
    }

    BadgeScreenScreen(
        uiState = uiState,
        onBack = onBack,
        onBadgeClick = { badgeCode -> viewModel.onIntent(BadgeUiIntent.ClickBadge(badgeCode = badgeCode)) },
        onDismissBadgeDetail = { viewModel.onIntent(BadgeUiIntent.DismissBadgeDetail) },
        modifier = modifier,
    )
}

@Composable
internal fun BadgeScreenScreen(
    uiState: BadgeUiState,
    onBack: () -> Unit,
    onBadgeClick: (badgeCode: String) -> Unit,
    onDismissBadgeDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        BadgeListContent(
            badges = uiState.badges,
            onBack = onBack,
            onBadgeClick = onBadgeClick,
        )

        uiState.selectedBadge?.let { badge ->
            BadgeDetailModal(
                badge = badge,
                badgeDetail = uiState.selectedBadgeDetail,
                isLoading = uiState.isBadgeDetailLoading,
                onDismiss = onDismissBadgeDetail,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@BasicPreview
@Composable
private fun BadgeScreenPreview() {
    PrezelTheme {
        BadgeScreenScreen(
            uiState = badgeScreenPreviewState(),
            onBack = {},
            onBadgeClick = {},
            onDismissBadgeDetail = {},
        )
    }
}
