package com.team.prezel.feature.my.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.badge.BadgeType
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.my.impl.component.BadgeSection
import com.team.prezel.feature.my.impl.component.MyTopAppBar
import com.team.prezel.feature.my.impl.component.ProfileSection
import com.team.prezel.feature.my.impl.contract.MyUiEffect
import com.team.prezel.feature.my.impl.contract.MyUiIntent
import com.team.prezel.feature.my.impl.contract.MyUiState
import com.team.prezel.feature.my.impl.model.BadgeUiModel
import com.team.prezel.feature.my.impl.model.MyUiMessage
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyScreen(
    navigateToEditProfile: () -> Unit,
    navigateToSetting: () -> Unit,
    navigateToBadge: () -> Unit,
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
    navigateToBadge: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MyTopAppBar(onClickSetting = onClickSetting)

        ProfileSection(uiState = uiState, onClickEditProfile = onClickEditProfile)

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        BadgeSection(
            badges = uiState.badges,
            onClickBadge = navigateToBadge,
        )
    }
}

@BasicPreview
@Composable
private fun MyScreenPreview() {
    PrezelTheme {
        MyScreen(
            uiState = MyUiState(
                profileImageUrl = null,
                nickname = "닉네임",
                badges = listOf(
                    BadgeType.FIRST_PRESENTATION,
                    BadgeType.SECOND_ANALYSIS,
                    BadgeType.FIRST_PRACTICE,
                    BadgeType.RETROSPECT_COMPLETED,
                    BadgeType.PERFECT_SCORE,
                    BadgeType.TEN_ANALYSIS,
                ).mapIndexed { index, type ->
                    BadgeUiModel(type = type, isAchieved = index % 2 == 0)
                }.toImmutableList(),
            ),
            onClickEditProfile = {},
            onClickSetting = {},
            navigateToBadge = {},
        )
    }
}
