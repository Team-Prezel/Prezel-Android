package com.team.prezel.feature.home.impl.main.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.floating.PrezelFloatingMenu
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.state.LocalAppDimmerState
import com.team.prezel.feature.home.impl.R
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
internal fun HomeAnalysisFabOverlay(
    onClickVoiceRecordingAnalysis: () -> Unit,
    onClickFileUploadAnalysis: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appDimmerState = LocalAppDimmerState.current
    val density = LocalDensity.current
    val fabEndPaddingPx = with(density) { PrezelTheme.spacing.V24.roundToPx() }
    var isFabExpanded by remember { mutableStateOf(false) }
    var collapsedFabBounds by remember { mutableStateOf<Rect?>(null) }
    var expandedMenuBounds by remember { mutableStateOf<Rect?>(null) }

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

    Box(modifier = modifier.fillMaxSize()) {
        HomeAnalysisFabAnchors(
            onCollapsedFabPositioned = { coordinates -> collapsedFabBounds = coordinates.boundsInWindow() },
            onExpandedMenuPositioned = { coordinates -> expandedMenuBounds = coordinates.boundsInWindow() },
        )

        HomeAnalysisFabPopup(
            collapsedFabBounds = collapsedFabBounds,
            expandedMenuBounds = expandedMenuBounds,
            fabEndPaddingPx = fabEndPaddingPx,
            density = density,
            isFabExpanded = isFabExpanded,
            onChangeExpanded = { isFabExpanded = it },
            onClickVoiceRecordingAnalysis = onClickVoiceRecordingAnalysis,
            onClickFileUploadAnalysis = onClickFileUploadAnalysis,
        )
    }
}

@Composable
private fun BoxScope.HomeAnalysisFabAnchors(
    onCollapsedFabPositioned: (LayoutCoordinates) -> Unit,
    onExpandedMenuPositioned: (LayoutCoordinates) -> Unit,
) {
    HomeAnalysisFabMeasurementAnchor(
        isExpanded = false,
        modifier = Modifier.align(Alignment.BottomEnd),
        onFabPositioned = onCollapsedFabPositioned,
    )
    HomeAnalysisFabMeasurementAnchor(
        isExpanded = true,
        modifier = Modifier.align(Alignment.BottomEnd),
        onFabPositioned = onExpandedMenuPositioned,
    )
}

@Composable
private fun HomeAnalysisFabPopup(
    collapsedFabBounds: Rect?,
    expandedMenuBounds: Rect?,
    fabEndPaddingPx: Int,
    density: Density,
    isFabExpanded: Boolean,
    onChangeExpanded: (Boolean) -> Unit,
    onClickVoiceRecordingAnalysis: () -> Unit,
    onClickFileUploadAnalysis: () -> Unit,
) {
    val expandedBounds = expandedMenuBounds ?: return
    val collapsedBounds = collapsedFabBounds ?: return
    val expandedMenuWidth = with(density) { expandedBounds.width.toDp() }
    val expandedMenuHeight = with(density) { expandedBounds.height.toDp() }

    Popup(
        popupPositionProvider = remember(collapsedBounds, expandedBounds, fabEndPaddingPx) {
            HomeFabPopupPositionProvider(
                collapsedFabBounds = collapsedBounds,
                expandedMenuBounds = expandedBounds,
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
                onChangeExpanded = onChangeExpanded,
                onClickVoiceRecording = {
                    onChangeExpanded(false)
                    onClickVoiceRecordingAnalysis()
                },
                onClickFileUpload = {
                    onChangeExpanded(false)
                    onClickFileUploadAnalysis()
                },
            )
        }
    }
}

@Composable
private fun HomeAnalysisFabMeasurementAnchor(
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onFabPositioned: (LayoutCoordinates) -> Unit,
) {
    HomeAnalysisFloatingMenu(
        isExpanded = isExpanded,
        onChangeExpanded = {},
        onClickVoiceRecording = {},
        onClickFileUpload = {},
        enabled = false,
        modifier = modifier
            .padding(end = PrezelTheme.spacing.V24, bottom = PrezelTheme.spacing.V24)
            .graphicsLayer { alpha = 0f }
            .clearAndSetSemantics { },
        onFabPositioned = onFabPositioned,
    )
}

@Composable
private fun HomeAnalysisFloatingMenu(
    isExpanded: Boolean,
    onChangeExpanded: (Boolean) -> Unit,
    onClickVoiceRecording: () -> Unit,
    onClickFileUpload: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
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
