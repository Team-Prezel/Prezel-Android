package com.team.prezel.feature.history.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.modal.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.ui.LocalSnackbarHostState
import com.team.prezel.feature.history.impl.component.HistoryEmptyContent
import com.team.prezel.feature.history.impl.component.HistoryHeadSection
import com.team.prezel.feature.history.impl.component.HistoryItemList
import com.team.prezel.feature.history.impl.component.historyTabs
import com.team.prezel.feature.history.impl.contract.HistoryUiEffect
import com.team.prezel.feature.history.impl.contract.HistoryUiIntent
import com.team.prezel.feature.history.impl.contract.HistoryUiState
import com.team.prezel.feature.history.impl.model.HistoryPresentationStatus
import com.team.prezel.feature.history.impl.model.HistoryUiMessage
import com.team.prezel.feature.history.impl.model.HistoryUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
internal fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabs = historyTabs()
    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }
    val snackbarHostState = LocalSnackbarHostState.current
    val resources = LocalResources.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(HistoryUiIntent.FetchData)

        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is HistoryUiEffect.ShowMessage -> {
                    val resId = when (effect.message) {
                        HistoryUiMessage.FETCH_DATA_FAILED -> R.string.feature_history_impl_fetch_data_failed
                    }
                    snackbarHostState.showPrezelSnackbar(message = resources.getString(resId))
                }
            }
        }
    }

    HistoryScreen(
        uiState = uiState,
        modifier = modifier,
        pagerState = pagerState,
        onClickHistoryItem = { item ->
            when (item.status) {
                HistoryPresentationStatus.PREPARING -> Unit
                HistoryPresentationStatus.COMPLETED -> Unit
            }
        },
    )
}

@Composable
internal fun HistoryScreen(
    uiState: HistoryUiState,
    pagerState: PagerState,
    onClickHistoryItem: (HistoryUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val tabs = historyTabs()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgMedium),
    ) {
        HistoryHeadSection(
            pagerState = pagerState,
            onClickTab = { pageIndex ->
                scope.launch { pagerState.animateScrollToPage(pageIndex) }
            },
            tabs = tabs,
        )

        HistoryContent(
            uiState = uiState,
            pagerState = pagerState,
            onClickHistoryItem = onClickHistoryItem,
        )
    }
}

@Composable
private fun HistoryContent(
    uiState: HistoryUiState,
    pagerState: PagerState,
    onClickHistoryItem: (HistoryUiModel) -> Unit,
) {
    when (uiState) {
        HistoryUiState.Loading -> Unit

        is HistoryUiState.Content -> {
            HistoryPagerContent(
                pagerState = pagerState,
                preparingPresentations = uiState.preparingPresentations,
                completedPresentations = uiState.completedPresentations,
                onClickHistoryItem = onClickHistoryItem,
            )
        }
    }
}

@Composable
private fun HistoryPagerContent(
    pagerState: PagerState,
    preparingPresentations: ImmutableList<HistoryUiModel>,
    completedPresentations: ImmutableList<HistoryUiModel>,
    onClickHistoryItem: (HistoryUiModel) -> Unit,
) {
    val pages = persistentListOf(
        preparingPresentations,
        completedPresentations,
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = false,
        overscrollEffect = null,
    ) { pageIndex ->
        val items = pages[pageIndex]

        if (items.isEmpty()) {
            HistoryEmptyContent(
                isPreparingTab = pageIndex == 0,
                onClickAddPresentation = { },
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            HistoryItemList(
                items = items,
                onClickItem = onClickHistoryItem,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@BasicPreview
@Composable
private fun HistoryScreenPreview() {
    val tabs = historyTabs()
    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }

    PrezelTheme {
        HistoryScreen(
            uiState = HistoryUiState.Content(
                preparingPresentations = persistentListOf(
                    HistoryUiModel(
                        id = 1L,
                        dDayLabel = "D-5",
                        dateLabel = "2026.04.19",
                        title = "캡스톤서비스기획 중간고사 발표",
                        category = Category.EDUCATION,
                        purpose = Purpose.CONTENT_DELIVERY,
                        style = Style.PROFESSIONAL,
                        audience = Audience.EXPERT,
                        status = HistoryPresentationStatus.PREPARING,
                    ),
                ),
                completedPresentations = persistentListOf(
                    HistoryUiModel(
                        id = 2L,
                        dDayLabel = "D+1",
                        dateLabel = "2026.04.12",
                        title = "서비스 런칭 회고 발표",
                        category = Category.PERSUASION,
                        purpose = Purpose.BUILD_EMPATHY,
                        style = Style.CALM,
                        audience = Audience.GENERAL_AUDIENCE,
                        status = HistoryPresentationStatus.COMPLETED,
                    ),
                ),
            ),
            pagerState = pagerState,
            onClickHistoryItem = { },
        )
    }
}
