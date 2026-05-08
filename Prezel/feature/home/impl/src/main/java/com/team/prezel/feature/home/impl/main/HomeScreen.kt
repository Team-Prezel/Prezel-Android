package com.team.prezel.feature.home.impl.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.core.ui.util.onHeightChanged
import com.team.prezel.feature.home.impl.R
import com.team.prezel.feature.home.impl.main.component.HomePageLayout
import com.team.prezel.feature.home.impl.main.component.body.EmptyPresentationSheet
import com.team.prezel.feature.home.impl.main.component.body.PresentationSheet
import com.team.prezel.feature.home.impl.main.component.head.HomeHeadSection
import com.team.prezel.feature.home.impl.main.component.title.EmptyPresentationHero
import com.team.prezel.feature.home.impl.main.component.title.PresentationHero
import com.team.prezel.feature.home.impl.main.contract.HomeUiEffect
import com.team.prezel.feature.home.impl.main.contract.HomeUiIntent
import com.team.prezel.feature.home.impl.main.contract.HomeUiState
import com.team.prezel.feature.home.impl.main.model.HomeUiMessage
import com.team.prezel.feature.home.impl.main.model.PresentationUiModel
import com.team.prezel.feature.home.impl.navigation.PracticeRecordingNavKey
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(0) { uiState.presentationCount() }
    val snackbarHostState = LocalSnackbarHostState.current
    val resources = LocalResources.current
    val navigator = LocalNavigator.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeUiIntent.FetchData)

        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is HomeUiEffect.ShowMessage -> {
                    val resId = when (effect.message) {
                        HomeUiMessage.FETCH_DATA_FAILED -> R.string.feature_home_impl_fetch_data_failed
                    }
                    snackbarHostState.showPrezelSnackbar(message = resources.getString(resId))
                }
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        pagerState = pagerState,
        onClickAddPresentation = { },
        onClickPracticeRecording = { navigator.navigate(PracticeRecordingNavKey) },
        onClickAnalyzePresentation = { },
        onClickWriteFeedback = { },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    pagerState: PagerState,
    onClickAddPresentation: () -> Unit,
    onClickPracticeRecording: () -> Unit,
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit,
    onClickWriteFeedback: (PresentationUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val maxScreenHeight = maxHeight
        var headerHeight by remember { mutableStateOf(0.dp) }

        Box(modifier = Modifier.fillMaxSize()) {
            HomeContent(
                uiState = uiState,
                pagerState = pagerState,
                maxHeight = maxScreenHeight,
                headerHeight = headerHeight,
                onClickAddPresentation = onClickAddPresentation,
                onClickPracticeRecording = onClickPracticeRecording,
                onClickAnalyzePresentation = onClickAnalyzePresentation,
                onClickWriteFeedback = onClickWriteFeedback,
            )

            HomeHeadSection(
                uiState = uiState,
                pagerState = pagerState,
                onClickTab = { pageIndex -> scope.launch { pagerState.scrollToPage(pageIndex) } },
                modifier = Modifier.onHeightChanged { newHeight -> headerHeight = newHeight },
            )
        }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    pagerState: PagerState,
    maxHeight: Dp,
    headerHeight: Dp,
    onClickAddPresentation: () -> Unit,
    onClickPracticeRecording: () -> Unit,
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit,
    onClickWriteFeedback: (PresentationUiModel) -> Unit,
) {
    when (uiState) {
        HomeUiState.Loading -> Unit
        is HomeUiState.Empty -> {
            HomeEmptyContent(
                maxHeight = maxHeight,
                headerHeight = headerHeight,
                uiState = uiState,
                onClickAddPresentation = onClickAddPresentation,
                onClickPracticeRecording = onClickPracticeRecording,
            )
        }

        is HomeUiState.SingleContent -> {
            HomeSingleContent(
                uiState = uiState,
                maxHeight = maxHeight,
                headerHeight = headerHeight,
                onClickPracticeRecording = onClickPracticeRecording,
                onClickAnalyzePresentation = onClickAnalyzePresentation,
                onClickWriteFeedback = onClickWriteFeedback,
            )
        }

        is HomeUiState.MultipleContent -> {
            HomeMultipleContent(
                uiState = uiState,
                pagerState = pagerState,
                maxHeight = maxHeight,
                headerHeight = headerHeight,
                onClickPracticeRecording = onClickPracticeRecording,
                onClickAnalyzePresentation = onClickAnalyzePresentation,
                onClickWriteFeedback = onClickWriteFeedback,
            )
        }
    }
}

@Composable
private fun HomeEmptyContent(
    maxHeight: Dp,
    headerHeight: Dp,
    uiState: HomeUiState.Empty,
    onClickAddPresentation: () -> Unit,
    onClickPracticeRecording: () -> Unit,
) {
    HomePageLayout(
        maxHeight = maxHeight,
        headerHeight = headerHeight,
        sheetContent = { EmptyPresentationSheet(onClickPracticeRecording = onClickPracticeRecording) },
        heroContent = {
            EmptyPresentationHero(
                nickname = uiState.nickname,
                onClickAddPresentation = onClickAddPresentation,
            )
        },
    )
}

@Composable
private fun HomeSingleContent(
    uiState: HomeUiState.SingleContent,
    maxHeight: Dp,
    headerHeight: Dp,
    onClickPracticeRecording: () -> Unit,
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit,
    onClickWriteFeedback: (PresentationUiModel) -> Unit,
) {
    val presentation = uiState.presentation

    HomePageLayout(
        maxHeight = maxHeight,
        headerHeight = headerHeight,
        sheetContent = {
            PresentationSheet(
                practiceCount = presentation.practiceCount,
                onClickPracticeRecording = onClickPracticeRecording,
            )
        },
        heroContent = {
            PresentationHero(
                presentation = presentation,
                onClickAnalyzePresentation = onClickAnalyzePresentation,
                onClickWriteFeedback = onClickWriteFeedback,
            )
        },
    )
}

@Composable
private fun HomeMultipleContent(
    uiState: HomeUiState.MultipleContent,
    pagerState: PagerState,
    maxHeight: Dp,
    headerHeight: Dp,
    onClickPracticeRecording: () -> Unit,
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit,
    onClickWriteFeedback: (PresentationUiModel) -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        overscrollEffect = null,
        userScrollEnabled = false,
        key = { pageIndex -> uiState.presentations[pageIndex].id },
    ) { pageIndex ->
        val presentation = uiState.presentations[pageIndex]

        HomePageLayout(
            maxHeight = maxHeight,
            headerHeight = headerHeight,
            sheetContent = {
                PresentationSheet(
                    practiceCount = presentation.practiceCount,
                    onClickPracticeRecording = onClickPracticeRecording,
                )
            },
            heroContent = {
                PresentationHero(
                    presentation = presentation,
                    onClickAnalyzePresentation = onClickAnalyzePresentation,
                    onClickWriteFeedback = onClickWriteFeedback,
                )
            },
        )
    }
}

@BasicPreview
@Composable
private fun HomeScreenEmptyPreview() {
    val uiState = HomeUiState.Empty(nickname = "프레즐")
    PrezelTheme {
        HomeScreen(
            uiState = uiState,
            pagerState = rememberPagerState(0) { uiState.presentationCount() },
            onClickAddPresentation = { },
            onClickPracticeRecording = { },
            onClickAnalyzePresentation = { },
            onClickWriteFeedback = { },
        )
    }
}

@BasicPreview
@Composable
private fun HomeScreenSinglePreview() {
    val uiState = HomeUiState.SingleContent(
        presentation = PresentationUiModel(
            id = 1L,
            category = Category.PERSUASION,
            title = "날짜 지난 발표제목",
            date = LocalDate(2026, 4, 3),
            dDay = -1,
        ),
    )
    PrezelTheme {
        HomeScreen(
            uiState = uiState,
            pagerState = rememberPagerState(0) { uiState.presentationCount() },
            onClickAddPresentation = { },
            onClickPracticeRecording = { },
            onClickAnalyzePresentation = { },
            onClickWriteFeedback = { },
        )
    }
}

@BasicPreview
@Composable
private fun HomeScreenMultiplePreview() {
    val uiState = HomeUiState.MultipleContent(
        List(3) { index ->
            PresentationUiModel(
                id = index.toLong(),
                category = Category.EDUCATION,
                title = "공백포함둘에서열글자",
                date = LocalDate(2026, 4, 10 + index),
                dDay = index,
            )
        }.toPersistentList(),
    )
    PrezelTheme {
        HomeScreen(
            uiState = uiState,
            pagerState = rememberPagerState(0) { uiState.presentationCount() },
            onClickAddPresentation = { },
            onClickPracticeRecording = { },
            onClickAnalyzePresentation = { },
            onClickWriteFeedback = { },
        )
    }
}
