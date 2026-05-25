package com.team.prezel.feature.report.impl.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.preview.ReportPreviewUpcomingUiState

private data class ReportTopBarState(
    val appBarHeight: Float,
    val headerTitleBottom: Float,
) {
    val isTitleVisible: Boolean
        get() = headerTitleBottom <= appBarHeight
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReportScreenLayout(
    appBarTitle: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable () -> Unit = {},
    trailingIcons: @Composable RowScope.() -> Unit = {},
    headerContent: @Composable ColumnScope.(Modifier) -> Unit,
    bodyContent: @Composable ColumnScope.() -> Unit,
) {
    val scrollState = rememberScrollState()
    val (topBarState, updateAppBarHeight, updateHeaderTitleBottom) = rememberReportTopBarState()

    Column(modifier = modifier.fillMaxSize()) {
        ReportDetailTopAppBar(
            appBarTitle = appBarTitle,
            topBarState = topBarState,
            onAppBarMeasured = updateAppBarHeight,
            leadingIcon = leadingIcon,
            trailingIcons = trailingIcons,
        )

        ReportDetailScrollContent(
            scrollState = scrollState,
            onHeaderMeasured = updateHeaderTitleBottom,
            headerContent = headerContent,
            bodyContent = bodyContent,
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportDetailTopAppBar(
    appBarTitle: String,
    topBarState: ReportTopBarState,
    onAppBarMeasured: (Float) -> Unit,
    leadingIcon: @Composable () -> Unit,
    trailingIcons: @Composable RowScope.() -> Unit,
) {
    PrezelTopAppBar(
        modifier = Modifier
            .background(PrezelTheme.colors.bgRegular)
            .onGloballyPositioned { coordinates ->
                onAppBarMeasured(coordinates.size.height.toFloat())
            },
        title = {
            AnimatedVisibility(
                visible = topBarState.isTitleVisible,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Text(
                    text = appBarTitle,
                    style = PrezelTheme.typography.body2Bold,
                    color = PrezelTheme.colors.textLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        leadingIcon = leadingIcon,
        trailingIcons = trailingIcons,
    )
}

@Composable
private fun ReportDetailScrollContent(
    scrollState: androidx.compose.foundation.ScrollState,
    onHeaderMeasured: (Float) -> Unit,
    headerContent: @Composable ColumnScope.(Modifier) -> Unit,
    bodyContent: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        HeaderContentContainer(
            onHeaderMeasured = onHeaderMeasured,
            headerContent = headerContent,
        )
        BodyContentContainer(bodyContent = bodyContent)
    }
}

@Composable
private fun HeaderContentContainer(
    onHeaderMeasured: (Float) -> Unit,
    headerContent: @Composable ColumnScope.(Modifier) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PrezelTheme.spacing.V20),
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
                )
            },
        )
    }
}
