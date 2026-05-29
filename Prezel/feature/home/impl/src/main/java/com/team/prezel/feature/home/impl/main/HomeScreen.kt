package com.team.prezel.feature.home.impl.main

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.core.ui.state.LocalAppDimmerState
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.core.ui.state.rememberAppDimmerState
import com.team.prezel.feature.home.impl.R
import com.team.prezel.feature.home.impl.main.component.HomeScreenContent
import com.team.prezel.feature.home.impl.main.contract.HomeUiEffect
import com.team.prezel.feature.home.impl.main.contract.HomeUiIntent
import com.team.prezel.feature.home.impl.main.contract.HomeUiState
import com.team.prezel.feature.home.impl.main.model.GrowthGraphData
import com.team.prezel.feature.home.impl.main.model.GrowthGraphItemUiModel
import com.team.prezel.feature.home.impl.main.model.HomeUiMessage
import com.team.prezel.feature.home.impl.main.model.PracticeRecordsUiModel
import com.team.prezel.feature.home.impl.main.model.PresentationUiModel
import com.team.prezel.feature.practice.api.PracticeNavKey
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDate

@Composable
internal fun HomeScreen(
    navigateToFileUploadAnalysis: () -> Unit,
    navigateToVoiceRecordingAnalysis: () -> Unit,
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

    HomeScreenContent(
        uiState = uiState,
        pagerState = pagerState,
        onClickAddPresentation = { },
        onClickPracticeRecording = { navigator.navigate(PracticeNavKey) },
        onClickAnalyzePresentation = { },
        onClickWriteFeedback = { },
        onClickVoiceRecordingAnalysis = navigateToVoiceRecordingAnalysis,
        onClickFileUploadAnalysis = navigateToFileUploadAnalysis,
        onClickCardGraphItemIndex = { presentationId, index ->
            viewModel.onIntent(HomeUiIntent.ClickCardGraphItem(presentationId = presentationId, index = index))
        },
        modifier = modifier,
    )
}

@BasicPreview
@Composable
private fun HomeScreenEmptyPreview() {
    val uiState = HomeUiState.Empty(nickname = "프레즐")
    HomeScreenPreview(uiState = uiState)
}

@BasicPreview
@Composable
private fun HomeScreenSinglePreview() {
    val uiState = HomeUiState.SingleContent(
        presentation = PresentationUiModel.Past(
            id = 1L,
            category = Category.OFFER,
            title = "날짜 지난 발표제목",
            date = LocalDate(2026, 4, 3),
            dDay = "D+1",
            practiceRecords = PracticeRecordsUiModel(
                practicedDates = listOf(LocalDate(2026, 4, 1), LocalDate(2026, 4, 3)),
                startDate = LocalDate(2026, 3, 30),
                endDate = LocalDate(2026, 4, 3),
            ),
            growthGraphData = GrowthGraphData(
                items = List(3) { index ->
                    GrowthGraphItemUiModel(attempt = index + 1, accuracyScore = 15.0 * index, scriptMatchRate = 10.0 * index)
                },
                selectedItemIndex = null,
            ),
        ),
    )
    HomeScreenPreview(uiState = uiState)
}

@BasicPreview
@Composable
private fun HomeScreenMultiplePreview() {
    val uiState = HomeUiState.MultipleContent(
        List(3) { index ->
            PresentationUiModel.Upcoming(
                id = index.toLong(),
                category = Category.EDUCATION,
                title = "공백포함둘에서열글자",
                date = LocalDate(2026, 4, 10 + index),
                dDay = "-$index",
                practiceRecords = PracticeRecordsUiModel(
                    practicedDates = listOf(LocalDate(2026, 4, 10 + index)),
                    startDate = LocalDate(2026, 4, 7 + index),
                    endDate = LocalDate(2026, 4, 10 + index),
                ),
            )
        }.toPersistentList(),
    )
    HomeScreenPreview(uiState = uiState)
}

@Composable
private fun HomeScreenPreview(uiState: HomeUiState) {
    PrezelTheme {
        CompositionLocalProvider(
            LocalAppDimmerState provides rememberAppDimmerState(),
        ) {
            HomeScreenContent(
                uiState = uiState,
                pagerState = rememberPagerState(0) { uiState.presentationCount() },
                onClickAddPresentation = {},
                onClickPracticeRecording = {},
                onClickAnalyzePresentation = {},
                onClickWriteFeedback = {},
                onClickVoiceRecordingAnalysis = {},
                onClickFileUploadAnalysis = {},
                onClickCardGraphItemIndex = { _, _ -> },
            )
        }
    }
}
