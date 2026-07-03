package com.team.prezel.core.designsystem.component.navigations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewDefaults
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun PrezelTabsPager(
    tabs: ImmutableList<String>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    size: PrezelTabSize = PrezelTabSize.REGULAR,
    userScrollEnabled: Boolean = true,
    content: @Composable (pageIndex: Int) -> Unit,
) {
    require(tabs.isNotEmpty()) { "tabs는 비어있을 수 없습니다." }
    require(pagerState.pageCount == tabs.size) {
        "pagerState.pageCount(${pagerState.pageCount})와 tabs.size(${tabs.size})가 일치해야 합니다."
    }

    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        PrezelTabs(
            tabs = tabs,
            pagerState = pagerState,
            size = size,
            onClickTab = { index ->
                handleTabClick(scope, pagerState, index)
            },
        )

        PrezelTabsPager(
            pagerState = pagerState,
            userScrollEnabled = userScrollEnabled,
            content = content,
        )
    }
}

private fun handleTabClick(
    scope: CoroutineScope,
    pagerState: PagerState,
    target: Int,
) {
    scope.launch {
        val current = pagerState.currentPage
        val distance = abs(current - target)

        if (distance <= 1) {
            pagerState.animateScrollToPage(target)
        } else {
            pagerState.scrollToPage(target)
        }
    }
}

@Composable
private fun PrezelTabsPager(
    pagerState: PagerState,
    userScrollEnabled: Boolean,
    content: @Composable (pageIndex: Int) -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        userScrollEnabled = userScrollEnabled,
        overscrollEffect = null,
    ) { page ->
        content(page)
    }
}

@BasicPreview
@Composable
private fun PrezelMediumTabPreview() {
    val tabs = persistentListOf("Label1", "Label2", "Label3")
    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }

    PreviewScaffold(
        defaults = PreviewDefaults(screenPadding = PaddingValues(0.dp)),
    ) {
        PrezelTabsPager(
            tabs = tabs,
            pagerState = pagerState,
            size = PrezelTabSize.SMALL,
            modifier = Modifier,
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Page: $page")
            }
        }
    }
}

@BasicPreview
@Composable
private fun PrezelRegularTabPreview() {
    val tabs = persistentListOf("Label1", "Label2")
    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }

    PreviewScaffold(
        defaults = PreviewDefaults(screenPadding = PaddingValues(0.dp)),
    ) {
        PrezelTabsPager(
            tabs = tabs,
            pagerState = pagerState,
            size = PrezelTabSize.REGULAR,
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Page: $page")
            }
        }
    }
}
