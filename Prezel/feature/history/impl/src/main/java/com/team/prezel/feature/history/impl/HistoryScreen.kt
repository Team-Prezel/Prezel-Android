package com.team.prezel.feature.history.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.component.modal.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.component.navigations.PrezelTabSize
import com.team.prezel.core.designsystem.component.navigations.PrezelTabsPager
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.ui.LocalSnackbarHostState
import com.team.prezel.feature.history.impl.component.HistoryEmptyContent
import com.team.prezel.feature.history.impl.component.HistoryItemList
import com.team.prezel.feature.history.impl.contract.HistoryUiEffect
import com.team.prezel.feature.history.impl.contract.HistoryUiIntent
import com.team.prezel.feature.history.impl.contract.HistoryUiState
import com.team.prezel.feature.history.impl.model.HistoryPageType
import com.team.prezel.feature.history.impl.model.HistoryPageUiModel
import com.team.prezel.feature.history.impl.model.HistoryUiMessage
import com.team.prezel.feature.history.impl.model.HistoryUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate

@Composable
internal fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val state = uiState
    val tabs = when (state) {
        HistoryUiState.Loading -> persistentListOf()
        is HistoryUiState.Content -> historyTabs(state.pages)
    }
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
        uiState = state,
        tabs = tabs,
        modifier = modifier,
        pagerState = pagerState,
        onClickHistoryItem = { },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScreen(
    uiState: HistoryUiState,
    tabs: ImmutableList<String>,
    pagerState: PagerState,
    onClickHistoryItem: (HistoryUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgMedium),
    ) {
        PrezelTopAppBar(
            modifier = Modifier.background(PrezelTheme.colors.bgRegular),
            title = { Text(text = stringResource(R.string.feature_history_impl_title)) },
        )

        HistoryContent(
            uiState = uiState,
            pagerState = pagerState,
            tabs = tabs,
            onClickHistoryItem = onClickHistoryItem,
        )
    }
}

@Composable
private fun HistoryContent(
    uiState: HistoryUiState,
    pagerState: PagerState,
    tabs: ImmutableList<String>,
    onClickHistoryItem: (HistoryUiModel) -> Unit,
) {
    when (uiState) {
        HistoryUiState.Loading -> Unit

        is HistoryUiState.Content -> {
            HistoryPagerContent(
                pagerState = pagerState,
                tabs = tabs,
                presentations = uiState.pages,
                onClickHistoryItem = onClickHistoryItem,
            )
        }
    }
}

@Composable
private fun HistoryPagerContent(
    pagerState: PagerState,
    tabs: ImmutableList<String>,
    presentations: ImmutableList<HistoryPageUiModel>,
    onClickHistoryItem: (HistoryUiModel) -> Unit,
) {
    PrezelTabsPager(
        tabs = tabs,
        pagerState = pagerState,
        size = PrezelTabSize.MEDIUM,
        modifier = Modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
        userScrollEnabled = false,
    ) { pageIndex ->
        val page = presentations.getOrElse(pageIndex) {
            error("Invalid page index: $pageIndex")
        }
        val items = page.items

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PrezelTheme.colors.bgMedium),
        ) {
            if (items.isEmpty()) {
                HistoryEmptyContent(
                    isPreparingTab = page.type == HistoryPageType.PREPARING,
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
}

@Composable
private fun historyTabs(pages: ImmutableList<HistoryPageUiModel>): ImmutableList<String> =
    pages
        .map { page ->
            when (page.type) {
                HistoryPageType.PREPARING -> stringResource(R.string.feature_history_impl_tab_preparing)
                HistoryPageType.COMPLETED -> stringResource(R.string.feature_history_impl_tab_completed)
            }
        }.toImmutableList()

@BasicPreview
@Composable
private fun HistoryScreenPreview() {
    val previewState = HistoryUiState.Content(
        pages = persistentListOf(
            HistoryPageUiModel(
                type = HistoryPageType.PREPARING,
                items = persistentListOf(
                    HistoryUiModel(
                        id = 1L,
                        dDay = 5,
                        date = LocalDate(2026, 4, 19),
                        title = "캡스톤서비스기획 중간고사 발표",
                        category = Category.EDUCATION,
                        purpose = Purpose.CONTENT_DELIVERY,
                        style = Style.PROFESSIONAL,
                        audience = Audience.EXPERT,
                    ),
                ),
            ),
            HistoryPageUiModel(
                type = HistoryPageType.COMPLETED,
                items = persistentListOf(
                    HistoryUiModel(
                        id = 2L,
                        dDay = -1,
                        date = LocalDate(2026, 4, 12),
                        title = "서비스 런칭 회고 발표",
                        category = Category.PERSUASION,
                        purpose = Purpose.BUILD_EMPATHY,
                        style = Style.CALM,
                        audience = Audience.GENERAL_AUDIENCE,
                    ),
                ),
            ),
        ),
    )
    val pagerState = rememberPagerState(initialPage = 0) { previewState.pages.size }

    PrezelTheme {
        HistoryScreen(
            uiState = previewState,
            tabs = historyTabs(previewState.pages),
            pagerState = pagerState,
            onClickHistoryItem = { },
        )
    }
}
