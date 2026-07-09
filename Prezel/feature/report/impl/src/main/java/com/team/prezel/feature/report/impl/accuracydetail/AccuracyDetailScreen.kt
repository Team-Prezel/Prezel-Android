package com.team.prezel.feature.report.impl.accuracydetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.component.navigations.PrezelTabSize
import com.team.prezel.core.designsystem.component.navigations.PrezelTabs
import com.team.prezel.core.designsystem.component.player.PrezelPlayerItem
import com.team.prezel.core.designsystem.component.player.PrezelPlayerState
import com.team.prezel.core.designsystem.component.player.rememberPrezelPlayerState
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.PresentationWordDetail
import com.team.prezel.core.model.presentation.SentenceAnalysisDetail
import com.team.prezel.core.model.presentation.WordAnalysisStatus
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.accuracydetail.component.AccuracyDetailPlayerSheet
import com.team.prezel.feature.report.impl.accuracydetail.component.AccuracyDetailTopAppBar
import com.team.prezel.feature.report.impl.accuracydetail.component.ScriptDetailList
import com.team.prezel.feature.report.impl.accuracydetail.component.toMarkerType
import com.team.prezel.feature.report.impl.accuracydetail.contract.AccuracyDetailUiEffect
import com.team.prezel.feature.report.impl.accuracydetail.contract.AccuracyDetailUiState
import com.team.prezel.feature.report.impl.accuracydetail.model.SentenceAnalysisUiModel
import com.team.prezel.feature.report.impl.accuracydetail.model.toUiModels
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlin.math.absoluteValue

@Serializable
internal enum class AccuracyDetailTab {
    SPEECH,
    SCRIPT_MATCH,
}

@Composable
internal fun AccuracyDetailScreen(
    onClose: () -> Unit,
    initialTab: AccuracyDetailTab,
    viewModel: AccuracyDetailViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current
    val resources = LocalResources.current

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is AccuracyDetailUiEffect.ShowMessage -> {
                    snackbarHostState.showPrezelSnackbar(
                        message = resources.getString(R.string.feature_report_impl_script_detail_load_failed),
                        useRaisedPosition = false,
                    )
                }
            }
        }
    }

    when (val state = uiState) {
        AccuracyDetailUiState.Loading -> Unit
        AccuracyDetailUiState.Error -> AccuracyDetailErrorScreen(onClose = onClose)
        is AccuracyDetailUiState.Content -> AccuracyDetailScreenContent(
            uiState = state,
            initialTab = initialTab,
            onClose = onClose,
        )
    }
}

@Composable
private fun AccuracyDetailErrorScreen(onClose: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        AccuracyDetailTopAppBar(onClose = onClose)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.feature_report_impl_script_detail_load_failed),
                style = PrezelTheme.typography.body2Regular,
                color = PrezelTheme.colors.textRegular,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccuracyDetailScreenContent(
    uiState: AccuracyDetailUiState.Content,
    initialTab: AccuracyDetailTab,
    onClose: () -> Unit,
    expandedSheet: Boolean = false,
) {
    val tabs = rememberAccuracyDetailTabs()
    val tabLabels = listOf(
        stringResource(R.string.feature_report_impl_label_speech),
        stringResource(R.string.feature_report_impl_label_script_match),
    ).toImmutableList()
    val pagerState = rememberPagerState(initialPage = initialTab.ordinal) { tabs.size }
    val selectedTab = tabs[pagerState.currentPage]
    val sentenceDetails = remember(uiState.wordDetail.sentenceDetails) {
        uiState.wordDetail.sentenceDetails.toUiModels()
    }
    val playerMarkerSentenceDetails = remember(selectedTab, sentenceDetails) {
        when (selectedTab) {
            AccuracyDetailTab.SPEECH -> sentenceDetails.filter { detail -> detail.isSpeechAccuracyIssue }
            AccuracyDetailTab.SCRIPT_MATCH -> sentenceDetails.filter { detail -> detail.isScriptMatchIssue }
        }.toImmutableList()
    }
    val sheetPeekHeight = AccuracyDetailPlayerSheetPeekHeight
    val playerState = rememberDetailPlayerState(
        selectedTab = selectedTab,
        sentenceDetails = sentenceDetails,
        markerSentenceDetails = playerMarkerSentenceDetails,
    )
    val playbackState = rememberRemoteAudioPlaybackState(audioUrl = uiState.wordDetail.audioUrl)
    val selectedSentence = remember(sentenceDetails, playerState.currentMillis) {
        sentenceDetails.firstOrNull { detail ->
            playerState.currentMillis in detail.startTimeMs..detail.endTimeMs
        }
    }
    val scaffoldState = rememberDetailScaffoldState(expandedSheet = expandedSheet)
    val isSheetExpanded = scaffoldState.isSheetExpanded

    PlaybackEffect(
        playerState = playerState,
        playbackState = playbackState,
    )

    AccuracyDetailScaffold(
        scaffoldState = scaffoldState,
        selectedTab = selectedTab,
        selectedSentence = selectedSentence,
        sentenceDetails = sentenceDetails,
        playerState = playerState,
        expanded = isSheetExpanded,
        sheetPeekHeight = sheetPeekHeight,
        onClose = onClose,
        tabLabels = tabLabels,
        onClickTab = { index -> pagerState.requestScrollToPage(index) },
        pagerState = pagerState,
    )
}

@Composable
private fun rememberAccuracyDetailTabs(): List<AccuracyDetailTab> =
    remember {
        listOf(
            AccuracyDetailTab.SPEECH,
            AccuracyDetailTab.SCRIPT_MATCH,
        )
    }

@Composable
private fun rememberDetailPlayerState(
    selectedTab: AccuracyDetailTab,
    sentenceDetails: ImmutableList<SentenceAnalysisUiModel>,
    markerSentenceDetails: ImmutableList<SentenceAnalysisUiModel>,
) = rememberPrezelPlayerState(
    durationMillis = remember(sentenceDetails) {
        sentenceDetails.maxOfOrNull { it.endTimeMs }?.coerceAtLeast(1L) ?: 1L
    },
    initialItems = remember(selectedTab, markerSentenceDetails) {
        markerSentenceDetails
            .map { detail ->
                PrezelPlayerItem.Marker(
                    timeMillis = detail.startTimeMs,
                    markerType = when (selectedTab) {
                        AccuracyDetailTab.SPEECH -> detail.speechAccuracyStatus.toMarkerType()
                        AccuracyDetailTab.SCRIPT_MATCH -> detail.scriptMatchStatus.toMarkerType()
                    },
                )
            }.toImmutableList()
    },
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberDetailScaffoldState(expandedSheet: Boolean): BottomSheetScaffoldState =
    rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = if (expandedSheet) {
                SheetValue.Expanded
            } else {
                SheetValue.PartiallyExpanded
            },
        ),
    )

@OptIn(ExperimentalMaterial3Api::class)
private val BottomSheetScaffoldState.isSheetExpanded: Boolean
    get() = bottomSheetState.currentValue == SheetValue.Expanded ||
        bottomSheetState.targetValue == SheetValue.Expanded

@Composable
private fun PlaybackEffect(
    playerState: PrezelPlayerState,
    playbackState: RemoteAudioPlaybackState,
) {
    LaunchedEffect(playerState.playing) {
        if (playerState.playing) {
            playbackState.play(startPositionMillis = playerState.currentMillis.toInt())
        } else {
            playbackState.pause()
        }
    }

    LaunchedEffect(playerState.currentMillis, playerState.playing) {
        if (!playerState.playing) return@LaunchedEffect

        val positionGap = (playerState.currentMillis - playbackState.currentPositionMillis).absoluteValue
        if (positionGap > SEEK_SYNC_THRESHOLD_MILLIS) {
            playbackState.seekTo(positionMillis = playerState.currentMillis.toInt())
        }
    }

    LaunchedEffect(playerState.playing) {
        while (playerState.playing) {
            delay(250L)
            playbackState.currentPositionMillis
                .takeIf { it > 0 }
                ?.let { playerState.updateCurrentMillis(it.toLong()) }
        }
    }

    LaunchedEffect(playbackState.playbackError) {
        if (playbackState.playbackError) playerState.pause()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccuracyDetailScaffold(
    scaffoldState: BottomSheetScaffoldState,
    selectedTab: AccuracyDetailTab,
    selectedSentence: SentenceAnalysisUiModel?,
    sentenceDetails: ImmutableList<SentenceAnalysisUiModel>,
    playerState: PrezelPlayerState,
    expanded: Boolean,
    sheetPeekHeight: Dp,
    onClose: () -> Unit,
    tabLabels: ImmutableList<String>,
    onClickTab: (Int) -> Unit,
    pagerState: PagerState,
) {
    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        sheetPeekHeight = sheetPeekHeight,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContainerColor = PrezelTheme.colors.solidWhite,
        sheetShadowElevation = 12.dp,
        sheetContent = {
            AccuracyDetailPlayerSheet(
                selectedTab = selectedTab,
                selectedSentence = selectedSentence,
                sentenceDetails = sentenceDetails,
                playerState = playerState,
                expanded = expanded,
            )
        },
        sheetDragHandle = null,
        containerColor = PrezelTheme.colors.bgRegular,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            AccuracyDetailTopAppBar(onClose = onClose)
            PrezelTabs(
                tabs = tabLabels,
                pagerState = pagerState,
                size = PrezelTabSize.SMALL,
                onClickTab = onClickTab,
            )
            ScriptDetailList(
                selectedTab = selectedTab,
                selectedSentence = selectedSentence,
                sentenceDetails = sentenceDetails,
            )
        }
    }
}

private const val SEEK_SYNC_THRESHOLD_MILLIS = 750L
private val AccuracyDetailPlayerSheetPeekHeight = 252.dp

@BasicPreview
@Composable
private fun AccuracyDetailSpeechPreview() {
    PrezelTheme {
        AccuracyDetailScreenContent(
            uiState = AccuracyDetailPreviewUiState,
            initialTab = AccuracyDetailTab.SPEECH,
            onClose = {},
        )
    }
}

@BasicPreview
@Composable
private fun AccuracyDetailScriptMatchPreview() {
    PrezelTheme {
        AccuracyDetailScreenContent(
            uiState = AccuracyDetailPreviewUiState,
            initialTab = AccuracyDetailTab.SCRIPT_MATCH,
            onClose = {},
        )
    }
}

@BasicPreview
@Composable
private fun AccuracyDetailExpandedBottomSheetPreview() {
    PrezelTheme {
        AccuracyDetailScreenContent(
            uiState = AccuracyDetailPreviewUiState,
            initialTab = AccuracyDetailTab.SPEECH,
            expandedSheet = true,
            onClose = {},
        )
    }
}

private val AccuracyDetailPreviewUiState = AccuracyDetailUiState.Content(
    wordDetail = PresentationWordDetail(
        presentationId = 1L,
        audioUrl = "https://example.com/audio.mp3",
        sentenceDetails = persistentListOf(
            SentenceAnalysisDetail(
                sentence = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
                status = WordAnalysisStatus.EXCELLENT,
                mainFeedback = "문장의 흐름이 깔끔했어요",
                subFeedback = "지금처럼 또렷한 말하기를 유지해주세요.",
                accuracy = 96.0,
                startTimeMs = 0L,
                endTimeMs = 1_800L,
                wordDetails = persistentListOf(),
            ),
            SentenceAnalysisDetail(
                sentence = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
                status = WordAnalysisStatus.INSERTION,
                mainFeedback = "같은 말을 반복하고 있어요.",
                subFeedback = "앞에서 했던 말은 반복하지 않는 것이 좋아요.",
                accuracy = 42.0,
                startTimeMs = 7_230L,
                endTimeMs = 8_700L,
                wordDetails = persistentListOf(),
            ),
            SentenceAnalysisDetail(
                sentence = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
                status = WordAnalysisStatus.OMISSION,
                mainFeedback = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
                subFeedback = "대본에 있으나 읽지 않은 구간이에요.",
                accuracy = 0.0,
                startTimeMs = 9_400L,
                endTimeMs = 11_300L,
                wordDetails = persistentListOf(),
            ),
        ),
    ),
)
