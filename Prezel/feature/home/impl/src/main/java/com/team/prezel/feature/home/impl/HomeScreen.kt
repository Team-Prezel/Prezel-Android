package com.team.prezel.feature.home.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.PrezelTabs
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.component.chip.PrezelChip
import com.team.prezel.core.designsystem.component.chip.PrezelChipColors
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.home.impl.contract.HomeUiIntent
import com.team.prezel.feature.home.impl.contract.HomeUiState
import com.team.prezel.feature.home.impl.model.CategoryUiModel
import com.team.prezel.feature.home.impl.model.PresentationUiModel
import com.team.prezel.feature.home.impl.model.backgroundRes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    onTabSelected: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeUiIntent.FetchData)
    }

    HomeScreen(
        uiState = uiState,
        onTabSelected = onTabSelected,
        onClickAddPresentation = { },
        onClickAnalyzePresentation = { },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onTabSelected: (String) -> Unit,
    onClickAddPresentation: () -> Unit,
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val bottomSheetHeight = 360.dp

    Column(modifier = modifier.fillMaxSize()) {
        PrezelTopAppBar(title = { Text(text = stringResource(R.string.feature_home_impl_title)) })

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            when (uiState) {
                HomeUiState.Loading -> {}
                is HomeUiState.Empty -> {
                    HomeEmptyContent(
                        nickname = uiState.nickname,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = bottomSheetHeight),
                        onClickAddPresentation = onClickAddPresentation,
                    )
                }

                is HomeUiState.SingleContent -> {
                    HomePresentationPage(
                        presentation = uiState.presentation,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = bottomSheetHeight),
                        onClickAnalyzePresentation = onClickAnalyzePresentation,
                    )
                }

                is HomeUiState.MultipleContent -> {
                    HomePresentationContent(
                        presentations = uiState.presentations,
                        onTabSelected = onTabSelected,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = bottomSheetHeight),
                        onClickAnalyzePresentation = onClickAnalyzePresentation,
                    )
                }
            }

            HomeBottomSheetShell(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(bottomSheetHeight),
            )
        }
    }
}

@Composable
private fun HomeBottomSheetShell(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(shape = PrezelTheme.shapes.V16)
            .background(color = PrezelTheme.colors.bgRegular)
            .padding(
                horizontal = PrezelTheme.spacing.V20,
                vertical = PrezelTheme.spacing.V32,
            ),
    ) {
        Text(
            text = stringResource(R.string.feature_home_impl_bottom_sheet_title),
            color = PrezelTheme.colors.textLarge,
            style = PrezelTheme.typography.body2Bold,
        )
    }
}

@Composable
private fun HomeEmptyContent(
    nickname: String,
    modifier: Modifier = Modifier,
    onClickAddPresentation: () -> Unit = {},
) {
    Box(
        modifier = modifier.paint(
            painterResource(id = com.team.prezel.core.designsystem.R.drawable.core_designsystem_section_title_empty),
            contentScale = ContentScale.FillBounds,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PrezelTheme.spacing.V20),
        ) {
            Text(
                text = stringResource(R.string.feature_home_impl_empty_greeting, nickname),
                color = PrezelTheme.colors.textMedium,
                style = PrezelTheme.typography.title1Medium,
            )
            Text(
                text = stringResource(R.string.feature_home_impl_empty_subtitle),
                color = PrezelTheme.colors.textLarge,
                style = PrezelTheme.typography.title1Bold,
            )
            Spacer(modifier = Modifier.weight(1f))
            PracticeActionButton(
                title = stringResource(R.string.feature_home_impl_add_presentation_title),
                actionText = stringResource(R.string.feature_home_impl_add_presentation_action),
                titleColor = PrezelTheme.colors.interactiveRegular,
                modifier = Modifier.fillMaxWidth(),
                onClick = onClickAddPresentation,
            )
        }
    }
}

@Composable
private fun HomePresentationContent(
    presentations: ImmutableList<PresentationUiModel>,
    onTabSelected: (String) -> Unit,
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = presentations.map(PresentationUiModel::dDayLabel).toPersistentList()
    val pagerState = rememberPagerState { presentations.size }

    LaunchedEffect(pagerState.currentPage, tabs) {
        onTabSelected(tabs[pagerState.currentPage])
    }

    PrezelTabs(
        tabs = tabs,
        pagerState = pagerState,
        modifier = modifier,
    ) { pageIndex ->
        HomePresentationPage(
            presentation = presentations[pageIndex],
            onClickAnalyzePresentation = onClickAnalyzePresentation,
        )
    }
}

@Composable
private fun HomePresentationPage(
    presentation: PresentationUiModel,
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.paint(
            painter = painterResource(id = presentation.category.backgroundRes()),
            contentScale = ContentScale.FillBounds,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = PrezelTheme.spacing.V20),
        ) {
            PrezelChip(
                text = presentation.category.label,
                customColors = PrezelChipColors(
                    containerColor = PrezelTheme.colors.bgRegular,
                    contentColor = PrezelTheme.colors.interactiveRegular,
                ),
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = presentation.date.toKoreanDate(),
                color = PrezelTheme.colors.textRegular,
                style = PrezelTheme.typography.body3Regular,
            )

            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))

            HomePresentationTitleRow(presentation = presentation)

            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))

            PracticeActionButton(
                title = stringResource(R.string.feature_home_impl_analyze_presentation_title),
                actionText = stringResource(R.string.feature_home_impl_analyze_presentation_action),
                titleColor = PrezelTheme.colors.textMedium,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onClickAnalyzePresentation(presentation) },
            )
        }
    }
}

@Composable
private fun HomePresentationTitleRow(presentation: PresentationUiModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = presentation.title,
            modifier = Modifier.weight(1f),
            color = PrezelTheme.colors.textLarge,
            style = PrezelTheme.typography.title1Bold,
        )
        Text(
            text = presentation.dDayLabel(),
            color = PrezelTheme.colors.interactiveRegular,
            style = PrezelTheme.typography.title1ExtraBold,
        )
    }
}

@Composable
private fun PracticeActionButton(
    title: String,
    actionText: String,
    titleColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    PrezelTouchArea(
        onClick = onClick,
        shape = PrezelTheme.shapes.V8,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape = PrezelTheme.shapes.V8)
                .border(
                    width = PrezelTheme.stroke.V1,
                    shape = PrezelTheme.shapes.V8,
                    color = PrezelTheme.colors.borderSmall,
                ).background(color = PrezelTheme.colors.bgRegular)
                .padding(horizontal = PrezelTheme.spacing.V16, vertical = PrezelTheme.spacing.V12),
        ) {
            Text(
                text = title,
                color = titleColor,
                style = PrezelTheme.typography.body2Bold,
            )

            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V6))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = actionText,
                    color = PrezelTheme.colors.textRegular,
                    style = PrezelTheme.typography.caption1Regular,
                )

                Icon(
                    painter = painterResource(PrezelIcons.ChevronRight),
                    contentDescription = actionText,
                    modifier = Modifier.size(16.dp),
                    tint = PrezelTheme.colors.iconRegular,
                )
            }
        }
    }
}

private fun LocalDate.toKoreanDate(): String = "${year}년 ${month.number.toString().padStart(2, '0')}월 ${day.toString().padStart(2, '0')}일"

private fun PresentationUiModel.dDayLabel(): String = "D-${dDay()}"

@BasicPreview
@Composable
private fun HomeEmptyContentPreview() {
    val uiState = HomeUiState.Empty(nickname = "프레즐")
    PrezelTheme {
        HomeScreen(
            uiState = uiState,
            onTabSelected = { },
            onClickAddPresentation = { },
            onClickAnalyzePresentation = { },
        )
    }
}

@BasicPreview
@Composable
private fun PresentationCardSinglePreview() {
    val uiState = HomeUiState.SingleContent(
        presentation = PresentationUiModel(
            id = 1L,
            category = CategoryUiModel.PERSUASION,
            title = "공백포함둘에서열글자",
            date = LocalDate(2026, 4, 10),
        ),
    )
    PrezelTheme {
        HomeScreen(
            uiState = uiState,
            onTabSelected = { },
            onClickAddPresentation = { },
            onClickAnalyzePresentation = { },
        )
    }
}

@BasicPreview
@Composable
private fun PresentationCardTabsPreview() {
    val uiState = HomeUiState.MultipleContent(
        List(3) { index ->
            PresentationUiModel(
                id = index.toLong(),
                category = CategoryUiModel.EDUCATION,
                title = "공백포함둘에서열글자",
                date = LocalDate(2026, 4, 10 + index),
            )
        }.toPersistentList(),
    )
    PrezelTheme {
        HomeScreen(
            uiState = uiState,
            onTabSelected = { },
            onClickAddPresentation = { },
            onClickAnalyzePresentation = { },
        )
    }
}
