package com.team.prezel.feature.report.impl.report.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.component.PrezelTopAppBarScope
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.report.preview.ReportPreviewUpcomingUiState

private data class ReportTopBarState(
    val appBarHeight: Float,
    val headerTitleBottom: Float,
) {
    val isTitleVisible: Boolean
        get() = headerTitleBottom <= appBarHeight
}

private const val TOP_APPBAR_VISIBILITY_SCROLL_THRESHOLD = 48
private val DefaultTopAppBarHeight = 56.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReportScreenLayout(
    appBarTitle: String,
    modifier: Modifier = Modifier,
    topAppBarContent: @Composable PrezelTopAppBarScope.() -> Unit = {},
    headerContent: @Composable ColumnScope.(Modifier) -> Unit,
    bodyContent: @Composable ColumnScope.() -> Unit,
) {
    val scrollState = rememberScrollState()
    val (topBarState, updateAppBarHeight, updateHeaderTitleBottom) = rememberReportTopBarState()
    val isTopAppBarVisible = rememberTopAppBarVisibility(scrollState)
    val density = LocalDensity.current
    val contentTopPadding = with(density) {
        topBarState.appBarHeight.takeIf { it > 0f }?.toDp() ?: DefaultTopAppBarHeight
    }

    Box(modifier = modifier.fillMaxSize()) {
        ReportDetailScrollContent(
            scrollState = scrollState,
            topPadding = contentTopPadding,
            onHeaderMeasured = updateHeaderTitleBottom,
            headerContent = headerContent,
            bodyContent = bodyContent,
        )

        AnimatedVisibility(
            visible = isTopAppBarVisible,
            enter = slideInVertically(initialOffsetY = { -it / 2 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it / 2 }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1f),
        ) {
            ReportDetailTopAppBar(
                appBarTitle = appBarTitle,
                topBarState = topBarState,
                onAppBarMeasured = updateAppBarHeight,
                content = topAppBarContent,
            )
        }
    }
}

@Composable
private fun rememberReportTopBarState(): Triple<ReportTopBarState, (Float) -> Unit, (Float) -> Unit> {
    var appBarHeight by remember { mutableFloatStateOf(0f) }
    var headerTitleBottom by remember { mutableFloatStateOf(Float.MAX_VALUE) }
    val topBarState by remember {
        derivedStateOf {
            ReportTopBarState(
                appBarHeight = appBarHeight,
                headerTitleBottom = headerTitleBottom,
            )
        }
    }

    return Triple(
        topBarState,
        { measuredHeight -> appBarHeight = measuredHeight },
        { measuredBottom -> headerTitleBottom = measuredBottom },
    )
}

@Composable
private fun rememberTopAppBarVisibility(scrollState: ScrollState): Boolean {
    var isVisible by remember { mutableStateOf(true) }
    var previousScrollOffset by remember { mutableIntStateOf(0) }
    var accumulatedScrollDelta by remember { mutableIntStateOf(0) }

    LaunchedEffect(scrollState.value) {
        val currentScrollOffset = scrollState.value
        val delta = currentScrollOffset - previousScrollOffset

        when {
            currentScrollOffset <= 0 -> {
                isVisible = true
                accumulatedScrollDelta = 0
            }
            delta == 0 -> Unit
            accumulatedScrollDelta == 0 || (accumulatedScrollDelta > 0) == (delta > 0) -> {
                accumulatedScrollDelta += delta
            }
            else -> {
                accumulatedScrollDelta = delta
            }
        }

        when {
            accumulatedScrollDelta >= TOP_APPBAR_VISIBILITY_SCROLL_THRESHOLD -> {
                isVisible = false
                accumulatedScrollDelta = 0
            }
            accumulatedScrollDelta <= -TOP_APPBAR_VISIBILITY_SCROLL_THRESHOLD -> {
                isVisible = true
                accumulatedScrollDelta = 0
            }
        }

        previousScrollOffset = currentScrollOffset
    }

    return isVisible
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportDetailTopAppBar(
    appBarTitle: String,
    topBarState: ReportTopBarState,
    onAppBarMeasured: (Float) -> Unit,
    content: @Composable PrezelTopAppBarScope.() -> Unit,
) {
    PrezelTopAppBar(
        title = if (topBarState.isTitleVisible) appBarTitle else null,
        modifier = Modifier
            .background(PrezelTheme.colors.bgRegular)
            .onGloballyPositioned { coordinates ->
                onAppBarMeasured(coordinates.size.height.toFloat())
            },
        content = content,
    )
}

@Composable
private fun ReportDetailScrollContent(
    scrollState: ScrollState,
    topPadding: androidx.compose.ui.unit.Dp,
    onHeaderMeasured: (Float) -> Unit,
    headerContent: @Composable ColumnScope.(Modifier) -> Unit,
    bodyContent: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        HeaderContentContainer(
            topPadding = topPadding,
            onHeaderMeasured = onHeaderMeasured,
            headerContent = headerContent,
        )
        BodyContentContainer(bodyContent = bodyContent)
    }
}

@Composable
private fun HeaderContentContainer(
    topPadding: androidx.compose.ui.unit.Dp,
    onHeaderMeasured: (Float) -> Unit,
    headerContent: @Composable ColumnScope.(Modifier) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = PrezelTheme.spacing.V20,
                end = PrezelTheme.spacing.V20,
                top = topPadding + PrezelTheme.spacing.V20,
                bottom = PrezelTheme.spacing.V20,
            ),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
    ) {
        headerContent(
            Modifier.onGloballyPositioned { coordinates ->
                val position = coordinates.positionInRoot().y
                onHeaderMeasured(position + coordinates.size.height)
            },
        )
    }
}

@Composable
private fun BodyContentContainer(bodyContent: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = PrezelTheme.spacing.V20,
                vertical = PrezelTheme.spacing.V32,
            ),
        verticalArrangement = Arrangement.spacedBy(64.dp),
        content = bodyContent,
    )
}

@BasicPreview
@Composable
private fun ReportScreenLayoutPreview() {
    PrezelTheme {
        ReportScreenLayout(
            appBarTitle = ReportPreviewUpcomingUiState.presentationInfo.title,
            headerContent = { headerTitleModifier ->
                ReportHeaderContent(
                    info = ReportPreviewUpcomingUiState.presentationInfo,
                    titleModifier = headerTitleModifier,
                )
            },
            bodyContent = {
                ReportBodyContent(
                    uiState = ReportPreviewUpcomingUiState,
                    onDeleteClick = { },
                    onImprovementCardIndexChange = {},
                    onReWriteScriptClick = {},
                    onReRecordingClick = {},
                    onFeedBackWriteClick = {},
                    onScriptAnalysisClick = {},
                    onSpeechAccuracyClick = {},
                    onScriptMatchClick = {},
                )
            },
        )
    }
}
