package com.team.prezel.feature.home.impl.main.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.ui.util.onHeightChanged
import com.team.prezel.feature.home.impl.main.component.body.EmptySheet
import com.team.prezel.feature.home.impl.main.component.body.PresentationSheet
import com.team.prezel.feature.home.impl.main.component.head.HomeHeadSection
import com.team.prezel.feature.home.impl.main.component.title.EmptyPresentationHero
import com.team.prezel.feature.home.impl.main.component.title.PresentationHero
import com.team.prezel.feature.home.impl.main.contract.HomeUiState
import com.team.prezel.feature.home.impl.main.model.PresentationUiModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreenContent(
    uiState: HomeUiState,
    pagerState: PagerState,
    onClickAddPresentation: () -> Unit,
    onClickPracticeRecording: (presentationId: Long) -> Unit,
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit,
    onClickWriteFeedback: (PresentationUiModel) -> Unit,
    onClickVoiceRecordingAnalysis: () -> Unit,
    onClickFileUploadAnalysis: () -> Unit,
    onClickCardGraphItemIndex: (presentationId: Long, index: Int) -> Unit,
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
                onClickCardGraphItemIndex = onClickCardGraphItemIndex,
            )

            HomeHeadSection(
                uiState = uiState,
                pagerState = pagerState,
                onClickTab = { pageIndex -> scope.launch { pagerState.scrollToPage(pageIndex) } },
                modifier = Modifier.onHeightChanged { newHeight -> headerHeight = newHeight },
            )

            HomeAnalysisFabOverlay(
                onClickVoiceRecordingAnalysis = onClickVoiceRecordingAnalysis,
                onClickFileUploadAnalysis = onClickFileUploadAnalysis,
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
    onClickPracticeRecording: (presentationId: Long) -> Unit,
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit,
    onClickWriteFeedback: (PresentationUiModel) -> Unit,
    onClickCardGraphItemIndex: (presentationId: Long, index: Int) -> Unit,
) {
    when (uiState) {
        HomeUiState.Loading -> Unit
        is HomeUiState.Empty -> {
            HomeEmptyContent(
                maxHeight = maxHeight,
                headerHeight = headerHeight,
                nickname = uiState.nickname,
                onClickAddPresentation = onClickAddPresentation,
            )
        }

        is HomeUiState.SingleContent -> {
            HomePresentationContent(
                presentation = uiState.presentation,
                maxHeight = maxHeight,
                headerHeight = headerHeight,
                onClickPracticeRecording = { onClickPracticeRecording(uiState.presentation.id) },
                onClickAnalyzePresentation = onClickAnalyzePresentation,
                onClickWriteFeedback = onClickWriteFeedback,
                onClickCardGraphItemIndex = { index -> onClickCardGraphItemIndex(uiState.presentation.id, index) },
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
                onClickCardGraphItemIndex = onClickCardGraphItemIndex,
            )
        }
    }
}

@Composable
private fun HomeEmptyContent(
    maxHeight: Dp,
    headerHeight: Dp,
    nickname: String,
    onClickAddPresentation: () -> Unit,
) {
    HomePageLayout(
        maxHeight = maxHeight,
        headerHeight = headerHeight,
        sheetContent = { EmptySheet() },
        heroContent = {
            EmptyPresentationHero(
                nickname = nickname,
                onClickAddPresentation = onClickAddPresentation,
            )
        },
    )
}

@Composable
private fun HomePresentationContent(
    presentation: PresentationUiModel,
    maxHeight: Dp,
    headerHeight: Dp,
    onClickPracticeRecording: () -> Unit,
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit,
    onClickWriteFeedback: (PresentationUiModel) -> Unit,
    onClickCardGraphItemIndex: (index: Int) -> Unit,
) {
    HomePageLayout(
        maxHeight = maxHeight,
        headerHeight = headerHeight,
        sheetContent = {
            PresentationSheet(
                presentation = presentation,
                onClickPracticeRecording = onClickPracticeRecording,
                onClickCardGraphItemIndex = onClickCardGraphItemIndex,
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
    onClickPracticeRecording: (presentationId: Long) -> Unit,
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit,
    onClickWriteFeedback: (PresentationUiModel) -> Unit,
    onClickCardGraphItemIndex: (presentationId: Long, index: Int) -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        overscrollEffect = null,
        userScrollEnabled = false,
        key = { pageIndex -> uiState.presentations.getOrNull(pageIndex)?.id ?: pageIndex },
    ) { pageIndex ->
        val presentation = uiState.presentations.getOrNull(pageIndex) ?: return@HorizontalPager

        HomePresentationContent(
            presentation = presentation,
            maxHeight = maxHeight,
            headerHeight = headerHeight,
            onClickPracticeRecording = { onClickPracticeRecording(presentation.id) },
            onClickAnalyzePresentation = onClickAnalyzePresentation,
            onClickWriteFeedback = onClickWriteFeedback,
            onClickCardGraphItemIndex = { index -> onClickCardGraphItemIndex(presentation.id, index) },
        )
    }
}
