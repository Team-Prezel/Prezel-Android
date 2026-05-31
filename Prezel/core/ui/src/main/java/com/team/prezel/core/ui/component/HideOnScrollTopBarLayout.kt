package com.team.prezel.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.math.abs

private const val DEFAULT_TOP_BAR_ANIMATION_DURATION_MILLIS = 200
private val DEFAULT_TOP_BAR_SCROLL_THRESHOLD = 48.dp

@Composable
fun HideOnScrollTopBarLayout(
    scrollState: ScrollState = rememberScrollState(),
    topBar: @Composable () -> Unit,
    body: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
    scrollThreshold: Dp = DEFAULT_TOP_BAR_SCROLL_THRESHOLD,
    animationDurationMillis: Int = DEFAULT_TOP_BAR_ANIMATION_DURATION_MILLIS,
) {
    val scrollThresholdPx = with(LocalDensity.current) { scrollThreshold.roundToPx() }
    var isTopBarVisible by remember { mutableStateOf(true) }
    var previousScrollPosition by remember { mutableIntStateOf(0) }
    var topBarToggleAnchorPosition by remember { mutableIntStateOf(0) }

    LaunchedEffect(scrollState, scrollThresholdPx) {
        snapshotFlow { scrollState.value }
            .map { currentScrollPosition ->
                val isAtTop = currentScrollPosition == 0
                val isScrollingUp = currentScrollPosition < previousScrollPosition
                val hasExceededThreshold =
                    abs(currentScrollPosition - topBarToggleAnchorPosition) >= scrollThresholdPx

                previousScrollPosition = currentScrollPosition

                when {
                    isAtTop -> {
                        topBarToggleAnchorPosition = currentScrollPosition
                        true
                    }
                    hasExceededThreshold && isScrollingUp -> {
                        topBarToggleAnchorPosition = currentScrollPosition
                        true
                    }
                    hasExceededThreshold && !isScrollingUp -> {
                        topBarToggleAnchorPosition = currentScrollPosition
                        false
                    }
                    else -> isTopBarVisible
                }
            }.distinctUntilChanged()
            .collect { visible ->
                isTopBarVisible = visible
            }
    }

    Column(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isTopBarVisible,
            enter = fadeIn(animationSpec = tween(animationDurationMillis)),
            exit = fadeOut(animationSpec = tween(animationDurationMillis)),
        ) {
            topBar()
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        ) {
            body()
        }

        bottomBar()
    }
}
