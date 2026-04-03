package com.team.prezel.feature.home.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.component.chip.PrezelChip
import com.team.prezel.core.designsystem.component.chip.PrezelChipInteraction
import com.team.prezel.core.designsystem.component.chip.PrezelChipStyle
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.home.impl.contract.HomeUiState
import com.team.prezel.feature.home.impl.model.CategoryUiModel
import com.team.prezel.feature.home.impl.model.PresentationUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    onTabSelected: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    modifier: Modifier,
    onClickAddPresentation: () -> Unit = {},
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxSize()) {
        PrezelTopAppBar(title = { Text(text = "홈") })

        when (uiState) {
            HomeUiState.Loading -> {}
            is HomeUiState.Empty -> {
                HomeEmptyContent(
                    nickname = uiState.nickname,
                    modifier = Modifier.fillMaxSize(),
                    onClickAddPresentation = onClickAddPresentation,
                )
            }

            is HomeUiState.Content -> {
                HomePresentationContent(
                    presentations = uiState.presentations,
                    modifier = Modifier.fillMaxSize(),
                    onClickAnalyzePresentation = onClickAnalyzePresentation,
                )
            }
        }
    }
}

@Composable
private fun HomeEmptyContent(
    nickname: String,
    modifier: Modifier = Modifier,
    onClickAddPresentation: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                PrezelTheme.spacing.V20,
            ),
    ) {
        Text(
            text = "안녕하세요 ${nickname}님!",
            color = PrezelTheme.colors.textMedium,
            style = PrezelTheme.typography.title1Medium,
        )
        Text(
            text = "어떤 발표를 앞두고 있나요?",
            color = PrezelTheme.colors.textLarge,
            style = PrezelTheme.typography.title1Bold,
        )
        Spacer(modifier = Modifier.weight(1f))
        HomeBottomCard(
            title = "발표 준비를 시작해볼까요?",
            actionText = "발표 추가하기",
            titleColor = PrezelTheme.colors.interactiveRegular,
            modifier = Modifier.fillMaxWidth(),
            onClick = onClickAddPresentation,
        )
    }
}

@Composable
private fun HomePresentationContent(
    presentations: ImmutableList<PresentationUiModel>,
    modifier: Modifier = Modifier,
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit = {},
) {
    val presentation = presentations.firstOrNull() ?: return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PrezelTheme.colors.bgMedium)
            .padding(all = PrezelTheme.spacing.V20),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PrezelChip(
                text = presentation.category.name,
                iconResId = PrezelIcons.College,
                style = PrezelChipStyle().copy(
                    interaction = PrezelChipInteraction.ACTIVE,
                ),
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = presentation.date.toKoreanDate(),
                color = PrezelTheme.colors.textRegular,
                style = PrezelTheme.typography.body3Regular,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

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
                text = "D-${presentation.dDay()}",
                color = PrezelTheme.colors.interactiveRegular,
                style = PrezelTheme.typography.title1Bold,
            )
        }

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))

        HomeBottomCard(
            title = "충분히 연습했는지 확인해볼까요?",
            actionText = "발표 분석하기",
            titleColor = PrezelTheme.colors.textMedium,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClickAnalyzePresentation(presentation) },
        )
    }
}

@Composable
private fun HomeBottomCard(
    title: String,
    actionText: String,
    titleColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    PrezelTouchArea(
        onClick = onClick,
        shape = PrezelTheme.shapes.V8,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape = PrezelTheme.shapes.V8)
                .background(color = PrezelTheme.colors.bgRegular)
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

private val previewItem = PresentationUiModel(
    id = "1",
    category = CategoryUiModel(
        iconResId = PrezelIcons.Blank,
        name = "카테고리",
    ),
    title = "공백포함둘에서열글자",
    date = LocalDate(2026, 4, 10),
)

private val previewItems = persistentListOf(
    previewItem,
    previewItem.copy(id = "2", date = LocalDate(2026, 4, 15)),
)

@BasicPreview
@Composable
private fun HomeEmptyContentPreview() {
    PrezelTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PrezelTheme.colors.bgMedium),
        ) {
            HomeEmptyContent(
                nickname = "Prezel",
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@BasicPreview
@Composable
private fun PresentationCardSinglePreview() {
    PrezelTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            HomePresentationContent(
                presentations = previewItems,
            )
        }
    }
}
