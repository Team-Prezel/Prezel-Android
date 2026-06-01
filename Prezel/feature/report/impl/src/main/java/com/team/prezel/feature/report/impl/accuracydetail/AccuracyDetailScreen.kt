package com.team.prezel.feature.report.impl.accuracydetail

import android.media.MediaPlayer
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
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
import com.team.prezel.core.model.presentation.WordAnalysisDetail
import com.team.prezel.core.model.presentation.WordAnalysisStatus
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.accuracydetail.component.AccuracyDetailPlayerSheet
import com.team.prezel.feature.report.impl.accuracydetail.component.AccuracyDetailTopAppBar
import com.team.prezel.feature.report.impl.accuracydetail.component.ScriptDetailList
import com.team.prezel.feature.report.impl.accuracydetail.component.isScriptMatchIssue
import com.team.prezel.feature.report.impl.accuracydetail.component.isSpeechAccuracySheetIssue
import com.team.prezel.feature.report.impl.accuracydetail.component.toMarkerType
import com.team.prezel.feature.report.impl.accuracydetail.contract.AccuracyDetailUiEffect
import com.team.prezel.feature.report.impl.accuracydetail.contract.AccuracyDetailUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

private const val PLAYER_TICK_MILLIS = 250L
private val AccuracyDetailSheetPeekHeight = 276.dp

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
    val wordDetails = uiState.wordDetail.wordDetails
    val playerMarkerWordDetails = remember(selectedTab, wordDetails) {
        wordDetails.playerMarkerDetailsFor(selectedTab)
    }
    val playerState = rememberDetailPlayerState(
        wordDetails = wordDetails,
        markerWordDetails = playerMarkerWordDetails,
    )
    val playbackState = rememberRemoteAudioPlaybackState(audioUrl = uiState.wordDetail.audioUrl)
    val selectedWord = remember(wordDetails, playerState.currentMillis) {
        wordDetails.currentDetailOrNull(currentMillis = playerState.currentMillis)
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
        selectedWord = selectedWord,
        wordDetails = wordDetails,
        playerState = playerState,
        expanded = isSheetExpanded,
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
    wordDetails: List<WordAnalysisDetail>,
    markerWordDetails: List<WordAnalysisDetail>,
) = rememberPrezelPlayerState(
    durationMillis = remember(wordDetails) {
        wordDetails.maxOfOrNull { it.endTimeMs }?.coerceAtLeast(1L) ?: 1L
    },
    initialItems = remember(markerWordDetails) {
        markerWordDetails
            .map { detail ->
                PrezelPlayerItem.Marker(
                    timeMillis = detail.startTimeMs,
                    markerType = detail.toMarkerType(),
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

    LaunchedEffect(playerState.playing) {
        while (playerState.playing) {
            delay(PLAYER_TICK_MILLIS)
            playbackState.currentPositionMillis
                .takeIf { it > 0 }
                ?.let { playerState.updateCurrentMillis(it.toLong()) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccuracyDetailScaffold(
    scaffoldState: BottomSheetScaffoldState,
    selectedTab: AccuracyDetailTab,
    selectedWord: WordAnalysisDetail?,
    wordDetails: List<WordAnalysisDetail>,
    playerState: PrezelPlayerState,
    expanded: Boolean,
    onClose: () -> Unit,
    tabLabels: ImmutableList<String>,
    onClickTab: (Int) -> Unit,
    pagerState: PagerState,
) {
    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        sheetPeekHeight = AccuracyDetailSheetPeekHeight,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContainerColor = PrezelTheme.colors.solidWhite,
        sheetShadowElevation = 12.dp,
        sheetContent = {
            AccuracyDetailPlayerSheet(
                selectedTab = selectedTab,
                selectedWord = selectedWord,
                wordDetails = wordDetails,
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
                size = PrezelTabSize.MEDIUM,
                onClickTab = onClickTab,
            )
            ScriptDetailList(
                selectedTab = selectedTab,
                selectedWord = selectedWord,
                wordDetails = wordDetails,
            )
        }
    }
}

@Composable
private fun rememberRemoteAudioPlaybackState(audioUrl: String): RemoteAudioPlaybackState {
    val state = remember(audioUrl) { RemoteAudioPlaybackState(audioUrl = audioUrl) }

    DisposableEffect(state) {
        onDispose { state.release() }
    }

    return state
}

private class RemoteAudioPlaybackState(
    private val audioUrl: String,
) {
    private var mediaPlayer: MediaPlayer? = null

    private var lastKnownPositionMillis by mutableIntStateOf(0)

    val currentPositionMillis: Int
        get() = mediaPlayer
            ?.currentPosition
            ?.coerceAtLeast(0)
            ?: lastKnownPositionMillis

    fun play(startPositionMillis: Int) {
        val player = mediaPlayer ?: preparePlayer() ?: return

        runCatching {
            player.seekTo(startPositionMillis.coerceAtLeast(0))
            player.start()
            lastKnownPositionMillis = player.currentPosition.coerceAtLeast(0)
        }.onFailure {
            release()
        }
    }

    fun pause() {
        mediaPlayer?.runCatching {
            if (isPlaying) pause()
            lastKnownPositionMillis = currentPosition.coerceAtLeast(0)
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        lastKnownPositionMillis = 0
    }

    private fun preparePlayer(): MediaPlayer? =
        runCatching {
            MediaPlayer().apply {
                setDataSource(audioUrl)
                prepare()
                setOnCompletionListener {
                    lastKnownPositionMillis = duration.coerceAtLeast(0)
                }
            }
        }.getOrNull()
            ?.also { mediaPlayer = it }
}

private fun List<WordAnalysisDetail>.currentDetailOrNull(currentMillis: Long): WordAnalysisDetail? =
    lastOrNull { detail -> currentMillis >= detail.startTimeMs } ?: firstOrNull()

private fun List<WordAnalysisDetail>.playerMarkerDetailsFor(tab: AccuracyDetailTab): List<WordAnalysisDetail> =
    when (tab) {
        AccuracyDetailTab.SPEECH -> filter { detail -> detail.isSpeechAccuracySheetIssue }
        AccuracyDetailTab.SCRIPT_MATCH -> filter { detail -> detail.isScriptMatchIssue }
    }

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
        wordDetails = listOf(
            WordAnalysisDetail(
                word = "문장의 흐름이 깔끔했어요",
                status = WordAnalysisStatus.EXCELLENT,
                description = "지금처럼 또렷한 말하기를 유지해주세요.",
                accuracy = 96.0,
                startTimeMs = 0L,
                endTimeMs = 1_800L,
            ),
            WordAnalysisDetail(
                word = "같은 말을 반복하고 있어요.",
                status = WordAnalysisStatus.INSERTION,
                description = "앞에서 했던 말은 반복하지 않는 것이 좋아요.",
                accuracy = 42.0,
                startTimeMs = 7_230L,
                endTimeMs = 8_700L,
            ),
            WordAnalysisDetail(
                word = "오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.",
                status = WordAnalysisStatus.OMISSION,
                description = "대본에 있으나 읽지 않은 구간이에요.",
                accuracy = 0.0,
                startTimeMs = 9_400L,
                endTimeMs = 11_300L,
            ),
        ),
    ),
)
