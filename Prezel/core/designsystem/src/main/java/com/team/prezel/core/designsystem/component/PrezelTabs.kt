package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

enum class PrezelTabSize { Small, Regular }

@Composable
fun PrezelTabs(
    tabs: ImmutableList<String>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    size: PrezelTabSize = PrezelTabSize.Regular,
    userScrollEnabled: Boolean = true,
    content: @Composable (pageIndex: Int) -> Unit,
) {
    require(tabs.isNotEmpty()) { "tabs는 비어있을 수 없습니다." }
    require(pagerState.pageCount == tabs.size) {
        "pagerState.pageCount(${pagerState.pageCount})와 tabs.size(${tabs.size})가 일치해야 합니다."
    }

    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        PrezelTabsBar(
            tabs = tabs,
            pagerState = pagerState,
            size = size,
            onTabClick = { index ->
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

@Composable
private fun PrezelTabsBar(
    tabs: ImmutableList<String>,
    pagerState: PagerState,
    size: PrezelTabSize,
    onTabClick: (index: Int) -> Unit,
) {
    SecondaryTabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
        indicator = {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .tabIndicatorOffset(selectedTabIndex = pagerState.currentPage)
                    .background(PrezelTheme.colors.solidBlack),
            )
        },
    ) {
        tabs.forEachIndexed { index, label ->
            PrezelTabContent(
                label = label,
                selected = pagerState.currentPage == index,
                size = size,
                onClick = { onTabClick(index) },
            )
        }
    }
}

@Composable
private fun PrezelTabContent(
    label: String,
    selected: Boolean,
    size: PrezelTabSize,
    onClick: () -> Unit,
) {
    Tab(
        selected = selected,
        onClick = onClick,
        modifier = Modifier.height(if (size == PrezelTabSize.Small) 36.dp else 48.dp),
        text = {
            Text(
                text = label,
                style = if (size == PrezelTabSize.Small) PrezelTextStyles.Body3Medium.toTextStyle() else PrezelTextStyles.Body2Bold.toTextStyle(),
            )
        },
        selectedContentColor = PrezelTheme.colors.solidBlack,
        unselectedContentColor = PrezelTheme.colors.textDisabled,
    )
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

@ThemePreview
@Composable
private fun PrezelMediumTabPreview() {
    val tabs = persistentListOf("Label1", "Label2", "Label3")
    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }

    PrezelTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PrezelTheme.colors.bgRegular),
        ) {
            PrezelTabs(
                tabs = tabs,
                pagerState = pagerState,
                size = PrezelTabSize.Regular,
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
}

@ThemePreview
@Composable
private fun PrezelSmallTabPreview() {
    val tabs = persistentListOf("Label1", "Label2")
    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }

    PrezelTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PrezelTheme.colors.bgRegular),
        ) {
            PrezelTabs(
                tabs = tabs,
                pagerState = pagerState,
                size = PrezelTabSize.Small,
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
}
