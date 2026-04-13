package com.team.prezel.feature.home.impl.component.head

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
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.feature.home.impl.R
import com.team.prezel.feature.home.impl.contract.HomeUiState
import com.team.prezel.feature.home.impl.model.PresentationUiModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeHeadSection(
    uiState: HomeUiState,
    pagerState: PagerState,
    onClickTab: (pageIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        PrezelTopAppBar(
            title = { Text(text = stringResource(R.string.feature_home_impl_title)) },
        )

        if (uiState is HomeUiState.MultipleContent) {
            PrezelTabs(
                pagerState = pagerState,
                tabs = uiState.dDayLabels,
                size = PrezelTabSize.REGULAR,
                onClickTab = onClickTab,
            )
        }
    }
}

@BasicPreview
@Composable
private fun HomeHeadSectionSinglePreview() {
    val uiState = HomeUiState.SingleContent(
        presentation = PresentationUiModel(
            id = 1L,
            category = Category.EDUCATION,
            title = "발표 1",
            date = LocalDate(2024, 1, 1),
            dDay = 0,
        ),
    )
    val pagerState = rememberPagerState(0) { uiState.presentationCount() }

    PrezelTheme {
        HomeHeadSection(
            uiState = uiState,
            pagerState = pagerState,
            onClickTab = {},
        )
    }
}

@BasicPreview
@Composable
private fun HomeHeadSectionMultiplePreview() {
    val uiState = HomeUiState.MultipleContent(
        presentations = listOf(
            PresentationUiModel(
                id = 1L,
                category = Category.EDUCATION,
                title = "발표 1",
                date = LocalDate(2024, 1, 1),
                dDay = 0,
            ),
            PresentationUiModel(
                id = 2L,
                category = Category.EVENT,
                title = "발표 2",
                date = LocalDate(2024, 1, 2),
                dDay = 1,
            ),
        ).toImmutableList(),
    )
    val pagerState = rememberPagerState(0) { uiState.presentationCount() }

    PrezelTheme {
        HomeHeadSection(
            uiState = uiState,
            pagerState = pagerState,
            {},
        )
    }
}
