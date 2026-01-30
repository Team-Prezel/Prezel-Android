package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Immutable
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

@Immutable
data class PrezelTabItem(
    val label: String,
    val badgeCount: Int? = null,
    val enabled: Boolean = true,
)

enum class PrezelTabSize { Small, Regular }

@Composable
fun PrezelTabs(
    tabs: ImmutableList<PrezelTabItem>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    size: PrezelTabSize = PrezelTabSize.Regular,
    userScrolledEnabled: Boolean = true,
    content: @Composable (pageIndex: Int) -> Unit,
) {
    require(tabs.isNotEmpty()) { "tabs는 비어있을 수 없습니다." }
    require(pagerState.pageCount == tabs.size) {
        "pagerState.pageCount(${pagerState.pageCount})와 tabs.size(${tabs.size})가 일치해야 합니다."
    }

    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        SecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            indicator = {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .tabIndicatorOffset(
                            selectedTabIndex = pagerState.currentPage,
                        ).background(PrezelTheme.colors.solidBlack),
                )
            },
        ) {
            tabs.forEachIndexed { index, item ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        if (!item.enabled) return@Tab
                        handleTabClick(scope, pagerState, index)
                    },
                    modifier = Modifier.height(if (size == PrezelTabSize.Small) 36.dp else 48.dp),
                    enabled = item.enabled,
                    text = {
                        PrezelTabLabel(
                            label = item.label,
                            size = size,
                            badgeCount = item.badgeCount,
                            selected = pagerState.currentPage == index,
                        )
                    },
                    selectedContentColor = PrezelTheme.colors.solidBlack,
                    unselectedContentColor = PrezelTheme.colors.textDisabled,
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = userScrolledEnabled,
            overscrollEffect = null,
        ) { page ->
            content(page)
        }
    }
}

@Composable
private fun PrezelTabLabel(
    label: String,
    size: PrezelTabSize,
    selected: Boolean,
    badgeCount: Int? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            style = if (size == PrezelTabSize.Small) PrezelTextStyles.Body3Medium.toTextStyle() else PrezelTextStyles.Body2Bold.toTextStyle(),
        )

        if (badgeCount != null) {
            PrezelBadge(
                active = false,
                count = badgeCount,
                size = PrezelBadgeSize.REGULAR,
                disabled = !selected,
            )
        }
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
    val tabs = persistentListOf(
        PrezelTabItem(label = "Label", badgeCount = 0),
        PrezelTabItem(label = "Label", badgeCount = 0),
        PrezelTabItem(label = "Label", badgeCount = 0),
    )
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
    val tabs = persistentListOf(
        PrezelTabItem(label = "Label", badgeCount = 2),
        PrezelTabItem(label = "Label", badgeCount = 3),
    )
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
