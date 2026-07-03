package com.team.prezel.core.designsystem.component.navigations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.designsystem.util.NoRippleInteractionSource
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PrezelTabs(
    tabs: ImmutableList<String>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    size: PrezelTabSize = PrezelTabSize.REGULAR,
    onClickTab: (index: Int) -> Unit,
) {
    SecondaryTabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
        indicator = { PrezelTabIndicator(selectedTabIndex = pagerState.currentPage) },
    ) {
        tabs.forEachIndexed { index, label ->
            PrezelTab(
                label = label,
                selected = pagerState.currentPage == index,
                size = size,
                onClick = { onClickTab(index) },
            )
        }
    }
}

@Composable
private fun TabIndicatorScope.PrezelTabIndicator(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(2.dp)
            .tabIndicatorOffset(selectedTabIndex = selectedTabIndex)
            .background(PrezelTheme.colors.solidBlack),
    )
}

@Composable
private fun PrezelTab(
    label: String,
    selected: Boolean,
    size: PrezelTabSize,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Tab(
        selected = selected,
        onClick = onClick,
        modifier = modifier.height(if (size == PrezelTabSize.SMALL) 36.dp else 48.dp),
        text = {
            Text(
                text = label,
                style = if (size == PrezelTabSize.SMALL) PrezelTextStyles.Body3Medium.toTextStyle() else PrezelTextStyles.Body2Bold.toTextStyle(),
            )
        },
        selectedContentColor = PrezelTheme.colors.solidBlack,
        unselectedContentColor = PrezelTheme.colors.textDisabled,
        interactionSource = NoRippleInteractionSource,
    )
}
