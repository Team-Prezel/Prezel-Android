package com.team.prezel.feature.badge.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.PrezelBadge
import com.team.prezel.feature.badge.impl.contract.BadgeUiState
import com.team.prezel.feature.badge.impl.model.BadgeUiModel
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BadgeScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BadgeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BadgeScreenContent(
        uiState = uiState,
        onBack = onBack,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BadgeScreenContent(
    uiState: BadgeUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PrezelTopAppBar(
            title = { Text(text = "나의 뱃지") },
            leadingIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(PrezelIcons.ChevronLeft),
                        contentDescription = "뒤로가기",
                    )
                }
            },
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = PrezelTheme.spacing.V16, horizontal = PrezelTheme.spacing.V20),
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
            horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
            overscrollEffect = null,
        ) {
            items(items = uiState.badges, key = { badge -> badge.badgeCode }) { badge ->
                PrezelBadge(
                    title = badge.badgeName,
                    url = badge.imageUrl,
                    isAchieved = badge.isUnlocked,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BadgeScreenPreview() {
    PrezelTheme {
        BadgeScreenContent(
            uiState = BadgeUiState(
                badges = persistentListOf(
                    BadgeUiModel(
                        badgeCode = "1",
                        badgeName = "첫 발표",
                        conditionText = "첫 발표를 완료하세요",
                        detailDescription = "첫 발표를 완료하면 획득할 수 있습니다.",
                        imageUrl = "",
                        isUnlocked = true,
                    ),
                    BadgeUiModel(
                        badgeCode = "2",
                        badgeName = "분석 왕",
                        conditionText = "발표 분석을 10번 완료하세요",
                        detailDescription = "발표 분석을 10번 완료하면 획득할 수 있습니다.",
                        imageUrl = "",
                        isUnlocked = false,
                    ),
                ),
                selectedBadgeCode = "1",
            ),
            onBack = {},
        )
    }
}
