package com.team.prezel.feature.home.impl.main.component

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.floating.PrezelFloatingMenu
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.ui.state.LocalAppDimmerState
import com.team.prezel.feature.home.impl.R
import kotlin.math.roundToInt

@Composable
internal fun HomeAnalysisFabOverlay(
    onClickVoiceRecordingAnalysis: () -> Unit,
    onClickFileUploadAnalysis: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val appDimmerState = LocalAppDimmerState.current
    var isFabExpanded by remember { mutableStateOf(false) }
    var fabBounds by remember { mutableStateOf<Rect?>(null) }

    LaunchedEffect(isFabExpanded, fabBounds, appDimmerState) {
        if (isFabExpanded) {
            val anchorBounds = fabBounds
            if (anchorBounds != null) {
                appDimmerState.show(
                    onDismissRequest = { isFabExpanded = false },
                    foregroundContent = {
                        HomeAnalysisFabForeground(
                            anchorBounds = anchorBounds,
                            onCollapse = { isFabExpanded = false },
                            onClickVoiceRecordingAnalysis = onClickVoiceRecordingAnalysis,
                            onClickFileUploadAnalysis = onClickFileUploadAnalysis,
                        )
                    },
                )
            }
        } else {
            appDimmerState.hide()
        }
    }

    DisposableEffect(appDimmerState) {
        onDispose { appDimmerState.hide() }
    }

    PrezelFloatingMenu(
        isExpanded = false,
        onChangeExpanded = { isFabExpanded = it },
        iconResId = PrezelIcons.Plus,
        openIconResId = PrezelIcons.Cancel,
        size = ButtonSize.REGULAR,
        hierarchy = ButtonHierarchy.PRIMARY,
        modifier = modifier.onGloballyPositioned { coordinates ->
            fabBounds = coordinates.boundsInWindow()
        },
    ) { }
}

@Composable
private fun BoxScope.HomeAnalysisFabForeground(
    anchorBounds: Rect,
    onCollapse: () -> Unit,
    onClickVoiceRecordingAnalysis: () -> Unit,
    onClickFileUploadAnalysis: () -> Unit,
) {
    var menuSize by remember { mutableStateOf(IntSize.Zero) }

    PrezelFloatingMenu(
        isExpanded = true,
        onChangeExpanded = { expanded ->
            if (!expanded) onCollapse()
        },
        iconResId = PrezelIcons.Plus,
        openIconResId = PrezelIcons.Cancel,
        size = ButtonSize.REGULAR,
        hierarchy = ButtonHierarchy.PRIMARY,
        modifier = Modifier
            .onGloballyPositioned { coordinates -> menuSize = coordinates.size }
            .then(
                if (menuSize == IntSize.Zero) {
                    Modifier
                } else {
                    Modifier.absoluteOffset {
                        IntOffset(
                            x = (anchorBounds.right - menuSize.width).roundToInt(),
                            y = (anchorBounds.bottom - menuSize.height).roundToInt(),
                        )
                    }
                },
            ),
    ) {
        MenuItem(
            label = stringResource(R.string.feature_home_impl_analysis_voice_recording),
            iconResId = PrezelIcons.Mic,
            onClick = {
                onCollapse()
                onClickVoiceRecordingAnalysis()
            },
        )
        MenuItem(
            label = stringResource(R.string.feature_home_impl_analysis_file_upload),
            iconResId = PrezelIcons.Folder,
            onClick = {
                onCollapse()
                onClickFileUploadAnalysis()
            },
        )
    }
}
