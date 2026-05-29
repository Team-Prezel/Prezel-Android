package com.team.prezel.feature.home.impl.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.floating.PrezelFloatingMenu
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.core.ui.state.LocalAppDimmerState
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
import com.team.prezel.feature.home.impl.main.model.GrowthGraphData
import com.team.prezel.feature.home.impl.main.model.HomeUiMessage
import com.team.prezel.feature.home.impl.main.model.PracticeRecordsUiModel
import com.team.prezel.feature.home.impl.main.model.PresentationUiModel
import com.team.prezel.feature.practice.api.PracticeNavKey
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.math.max
import kotlin.math.roundToInt

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

    HomeScreen(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    pagerState: PagerState,
    onClickAddPresentation: () -> Unit,
    onClickPracticeRecording: () -> Unit,
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit,
    onClickWriteFeedback: (PresentationUiModel) -> Unit,
    onClickVoiceRecordingAnalysis: () -> Unit,
    onClickFileUploadAnalysis: () -> Unit,
    onClickCardGraphItemIndex: (presentationId: Long, index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val appDimmerState = LocalAppDimmerState.current
    val density = LocalDensity.current
    val fabEndPaddingPx = with(density) { PrezelTheme.spacing.V24.roundToPx() }
    var isFabExpanded by remember { mutableStateOf(false) }
    var collapsedFabBounds by remember { mutableStateOf<Rect?>(null) }
    var expandedMenuBounds by remember { mutableStateOf<Rect?>(null) }
    val onClickVoiceRecording = {
        isFabExpanded = false
        onClickVoiceRecordingAnalysis()
    }
    val onClickFileUpload = {
        isFabExpanded = false
        onClickFileUploadAnalysis()
    }

    LaunchedEffect(isFabExpanded) {
        if (isFabExpanded) {
            appDimmerState.show { isFabExpanded = false }
        } else {
            appDimmerState.hide()
        }
    }

    DisposableEffect(appDimmerState) {
        onDispose { appDimmerState.hide() }
    }

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

            HomeAnalysisFloatingMenu(
                isExpanded = false,
                onChangeExpanded = {},
                onClickVoiceRecording = {},
                onClickFileUpload = {},
                enabled = false,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = PrezelTheme.spacing.V24, bottom = PrezelTheme.spacing.V24)
                    .graphicsLayer { alpha = 0f }
                    .clearAndSetSemantics { },
                onFabPositioned = { coordinates -> collapsedFabBounds = coordinates.boundsInWindow() },
            )

            HomeAnalysisFloatingMenu(
                isExpanded = true,
                onChangeExpanded = {},
                onClickVoiceRecording = {},
                onClickFileUpload = {},
                enabled = false,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = PrezelTheme.spacing.V24, bottom = PrezelTheme.spacing.V24)
                    .graphicsLayer { alpha = 0f }
                    .clearAndSetSemantics { },
                onFabPositioned = { coordinates -> expandedMenuBounds = coordinates.boundsInWindow() },
            )

            if (collapsedFabBounds != null && expandedMenuBounds != null) {
                val expandedMenuWidth = with(density) { expandedMenuBounds!!.width.toDp() }
                val expandedMenuHeight = with(density) { expandedMenuBounds!!.height.toDp() }

                Popup(
                    popupPositionProvider = remember(collapsedFabBounds, expandedMenuBounds, fabEndPaddingPx) {
                        HomeFabPopupPositionProvider(
                            collapsedFabBounds = collapsedFabBounds!!,
                            expandedMenuBounds = expandedMenuBounds!!,
                            endPaddingPx = fabEndPaddingPx,
                        )
                    },
                ) {
                    Box(
                        modifier = Modifier.requiredSize(
                            width = expandedMenuWidth,
                            height = expandedMenuHeight,
                        ),
                        contentAlignment = Alignment.BottomEnd,
                    ) {
                        HomeAnalysisFloatingMenu(
                            isExpanded = isFabExpanded,
                            onChangeExpanded = { isFabExpanded = it },
                            onClickVoiceRecording = onClickVoiceRecording,
                            onClickFileUpload = onClickFileUpload,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeAnalysisFloatingMenu(
    isExpanded: Boolean,
    onChangeExpanded: (Boolean) -> Unit,
    onClickVoiceRecording: () -> Unit,
    onClickFileUpload: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onFabPositioned: ((LayoutCoordinates) -> Unit)? = null,
) {
    PrezelFloatingMenu(
        isExpanded = isExpanded,
        onChangeExpanded = onChangeExpanded,
        iconResId = PrezelIcons.Plus,
        openIconResId = PrezelIcons.Cancel,
        size = ButtonSize.REGULAR,
        hierarchy = ButtonHierarchy.PRIMARY,
        enabled = enabled,
        modifier = modifier.then(
            if (onFabPositioned == null) {
                Modifier
            } else {
                Modifier.onGloballyPositioned(onFabPositioned)
            },
        ),
    ) {
        MenuItem(
            label = stringResource(R.string.feature_home_impl_analysis_voice_recording),
            iconResId = PrezelIcons.Mic,
            onClick = onClickVoiceRecording,
        )
        MenuItem(
            label = stringResource(R.string.feature_home_impl_analysis_file_upload),
            iconResId = PrezelIcons.Folder,
            onClick = onClickFileUpload,
        )
    }
}

private class HomeFabPopupPositionProvider(
    private val collapsedFabBounds: Rect,
    private val expandedMenuBounds: Rect,
    private val endPaddingPx: Int,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val x = windowSize.width - popupContentSize.width - endPaddingPx
        val y = (collapsedFabBounds.bottom - expandedMenuBounds.height).roundToInt()

        return IntOffset(
            x = max(0, x),
            y = max(0, y),
        )
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
    onClickCardGraphItemIndex: (presentationId: Long, index: Int) -> Unit,
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
    onClickCardGraphItemIndex: (index: Int) -> Unit,
) {
    HomePageLayout(
        maxHeight = maxHeight,
        headerHeight = headerHeight,
        sheetContent = {
            PresentationSheet(
                presentation = uiState.presentation,
                onClickPracticeRecording = onClickPracticeRecording,
                onClickCardGraphItemIndex = onClickCardGraphItemIndex,
            )
        },
        heroContent = {
            PresentationHero(
                presentation = uiState.presentation,
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
    onClickCardGraphItemIndex: (presentationId: Long, index: Int) -> Unit,
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
                    presentation = presentation,
                    onClickPracticeRecording = onClickPracticeRecording,
                    onClickCardGraphItemIndex = { index -> onClickCardGraphItemIndex(presentation.id, index) },
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
            onClickVoiceRecordingAnalysis = { },
            onClickFileUploadAnalysis = { },
            onClickCardGraphItemIndex = { presentationId, index -> },
        )
    }
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
            dDay = "+1",
            practiceRecords = PracticeRecordsUiModel(
                practicedDates = listOf(LocalDate(2026, 4, 1), LocalDate(2026, 4, 3)),
                startDate = LocalDate(2026, 3, 30),
                endDate = LocalDate(2026, 4, 3),
            ),
            growthGraphData = GrowthGraphData(items = emptyList(), selectedItemIndex = 0),
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
            onClickVoiceRecordingAnalysis = { },
            onClickFileUploadAnalysis = { },
            onClickCardGraphItemIndex = { presentationId, index -> },
        )
    }
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
    PrezelTheme {
        HomeScreen(
            uiState = uiState,
            pagerState = rememberPagerState(0) { uiState.presentationCount() },
            onClickAddPresentation = { },
            onClickPracticeRecording = { },
            onClickAnalyzePresentation = { },
            onClickWriteFeedback = { },
            onClickVoiceRecordingAnalysis = { },
            onClickFileUploadAnalysis = { },
            onClickCardGraphItemIndex = { presentationId, index -> },
        )
    }
}
