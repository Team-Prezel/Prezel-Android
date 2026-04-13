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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.modal.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.LocalSnackbarHostState
import com.team.prezel.feature.history.impl.component.HistoryHeadSection
import com.team.prezel.feature.history.impl.component.HistoryItemList
import com.team.prezel.feature.history.impl.component.historyTabs
import com.team.prezel.feature.history.impl.contract.HistoryUiEffect
import com.team.prezel.feature.history.impl.contract.HistoryUiIntent
import com.team.prezel.feature.history.impl.contract.HistoryUiState
import com.team.prezel.feature.history.impl.model.HistoryChipUiModel
import com.team.prezel.feature.history.impl.model.HistoryUiMessage
import com.team.prezel.feature.history.impl.model.PresentationItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
internal fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(initialPage = 0) { historyTabs.size }
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
    )
}

@Composable
internal fun HistoryScreen(
    uiState: HistoryUiState,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val pages = remember(uiState) { historyPreviewPages() }

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
            tabs = historyTabs,
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = false,
            overscrollEffect = null,
        ) { pageIndex ->
            HistoryItemList(
                items = pages[pageIndex],
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

private fun historyPreviewPages(): ImmutableList<ImmutableList<PresentationItem>> =
    persistentListOf(
        persistentListOf(
            PresentationItem(
                id = 1L,
                dDayLabel = "D-5",
                dateLabel = "2026.04.19",
                title = "캡스톤서비스기획 중간고사 발표",
                chips = persistentListOf(
                    HistoryChipUiModel(label = "학술·교육", highlighted = true),
                    HistoryChipUiModel(label = "내용 전달"),
                    HistoryChipUiModel(label = "논리적"),
                    HistoryChipUiModel(label = "전문가"),
                ),
            ),
            PresentationItem(
                id = 2L,
                dDayLabel = "D-7",
                dateLabel = "2026.04.21",
                title = "IT동아리 대규모 세미나",
                chips = persistentListOf(
                    HistoryChipUiModel(label = "업무·보고", highlighted = true),
                    HistoryChipUiModel(label = "내용 전달"),
                    HistoryChipUiModel(label = "논리적"),
                    HistoryChipUiModel(label = "일반 청중"),
                ),
            ),
        ),
        persistentListOf(
            PresentationItem(
                id = 3L,
                dDayLabel = "D+1",
                dateLabel = "2026.04.12",
                title = "서비스 런칭 회고 발표",
                chips = persistentListOf(
                    HistoryChipUiModel(label = "업무·보고", highlighted = true),
                    HistoryChipUiModel(label = "분석형"),
                    HistoryChipUiModel(label = "팀 회고"),
                ),
            ),
            PresentationItem(
                id = 4L,
                dDayLabel = "D+12",
                dateLabel = "2026.04.02",
                title = "졸업 프로젝트 최종 발표",
                chips = persistentListOf(
                    HistoryChipUiModel(label = "학술·교육", highlighted = true),
                    HistoryChipUiModel(label = "스토리텔링"),
                    HistoryChipUiModel(label = "전문가"),
                ),
            ),
        ),
    )

@BasicPreview
@Composable
private fun HistoryScreenPreview() {
    val pagerState = rememberPagerState(initialPage = 0) { historyTabs.size }

    PrezelTheme {
        HistoryScreen(uiState = HistoryUiState.Loading, pagerState = pagerState)
    }
}
