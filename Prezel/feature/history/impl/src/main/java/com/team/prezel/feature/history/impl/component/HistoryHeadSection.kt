package com.team.prezel.feature.history.impl.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.component.navigations.PrezelTabSize
import com.team.prezel.core.designsystem.component.navigations.PrezelTabs
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.history.impl.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryHeadSection(
    pagerState: PagerState,
    onClickTab: (pageIndex: Int) -> Unit,
    tabs: ImmutableList<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        PrezelTopAppBar(
            title = { Text(text = stringResource(R.string.feature_history_impl_title)) },
        )

        PrezelTabs(
            tabs = tabs,
            pagerState = pagerState,
            size = PrezelTabSize.MEDIUM,
            onClickTab = onClickTab,
        )
    }
}

@BasicPreview
@Composable
private fun HistoryHeadSectionPreview() {
    PrezelTheme {
        HistoryHeadSection(
            pagerState = rememberPagerState(initialPage = 0) { historyTabs.size },
            onClickTab = { },
            tabs = historyTabs,
        )
    }
}

internal val historyTabs = persistentListOf("준비 중인 발표", "완료한 발표")
