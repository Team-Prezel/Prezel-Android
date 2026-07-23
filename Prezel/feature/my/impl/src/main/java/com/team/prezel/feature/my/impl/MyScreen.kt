package com.team.prezel.feature.my.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.component.list.PrezelList
import com.team.prezel.core.designsystem.component.list.PrezelListSize
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.PrezelBadge
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.core.ui.util.noRippleClickable
import com.team.prezel.feature.my.impl.component.MyTopAppBar
import com.team.prezel.feature.my.impl.component.ProfileSection
import com.team.prezel.feature.my.impl.contract.MyUiEffect
import com.team.prezel.feature.my.impl.contract.MyUiIntent
import com.team.prezel.feature.my.impl.contract.MyUiState
import com.team.prezel.feature.my.impl.model.BadgeUiModel
import com.team.prezel.feature.my.impl.model.MyUiMessage
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun MyScreen(
    navigateToEditProfile: () -> Unit,
    navigateToSetting: () -> Unit,
    navigateToBadge: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current
    val resources = LocalResources.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(MyUiIntent.FetchData)

        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is MyUiEffect.ShowMessage -> {
                    val resId = when (effect.message) {
                        MyUiMessage.FETCH_USER_INFO_FAILED -> R.string.feature_my_impl_message_fetch_user_info_failed
                        MyUiMessage.FETCH_USER_BADGES_FAILED -> R.string.feature_my_impl_message_fetch_user_badges_failed
                    }

                    snackbarHostState.showPrezelSnackbar(message = resources.getString(resId))
                }
            }
        }
    }

    MyScreen(
        uiState = uiState,
        onClickEditProfile = navigateToEditProfile,
        onClickSetting = navigateToSetting,
        navigateToBadge = navigateToBadge,
        modifier = modifier,
    )
}

@Composable
private fun MyScreen(
    uiState: MyUiState,
    onClickEditProfile: () -> Unit,
    onClickSetting: () -> Unit,
    navigateToBadge: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MyTopAppBar(onClickSetting = onClickSetting)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                vertical = PrezelTheme.spacing.V16,
                horizontal = PrezelTheme.spacing.V20,
            ),
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
            horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                ProfileSection(
                    nickname = uiState.nickname,
                    profileImageUrl = uiState.profileImageUrl,
                    onClickEditProfile = onClickEditProfile,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                BadgeListTitle(
                    modifier = Modifier
                        .padding(top = PrezelTheme.spacing.V16)
                        .fillMaxWidth(),
                )
            }

            items(
                items = uiState.badges,
                key = { badge -> badge.code },
            ) { badge ->
                BadgeGridItem(
                    badge = badge,
                    modifier = Modifier.noRippleClickable(onClick = { navigateToBadge(badge.code) }),
                )
            }
        }
    }
}

@Composable
private fun BadgeListTitle(modifier: Modifier = Modifier) {
    PrezelList(
        title = "나의 뱃지",
        titleTextColor = PrezelTheme.colors.textLarge,
        size = PrezelListSize.REGULAR,
        nested = true,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
private fun BadgeGridItem(
    badge: BadgeUiModel,
    modifier: Modifier = Modifier,
) {
    PrezelBadge(
        title = badge.title,
        url = badge.imageUrl,
        isAchieved = badge.isAchieved,
        modifier = modifier.fillMaxWidth(),
    )
}

@BasicPreview
@Composable
private fun MyScreenPreview() {
    PrezelTheme {
        MyScreen(
            uiState = MyUiState(
                profileImageUrl = null,
                nickname = "닉네임",
                badges = List(6) { index ->
                    BadgeUiModel(
                        code = index.toString(),
                        title = index.toString(),
                        imageUrl = "",
                        isAchieved = index % 2 == 0,
                    )
                }.toImmutableList(),
            ),
            onClickEditProfile = {},
            onClickSetting = {},
            navigateToBadge = {},
        )
    }
}
