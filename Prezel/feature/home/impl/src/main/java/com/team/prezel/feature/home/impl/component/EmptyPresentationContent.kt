package com.team.prezel.feature.home.impl.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.home.impl.R
import com.team.prezel.feature.home.impl.component.body.HomeBottomSheetScaffold
import com.team.prezel.feature.home.impl.component.title.HomeTitleSection
import com.team.prezel.feature.home.impl.component.title.PracticeActionCard
import com.team.prezel.feature.home.impl.contract.HomeUiState

@Composable
internal fun EmptyPresentationContent(
    uiState: HomeUiState.Empty,
    maxHeight: Dp,
    headerHeight: Dp,
    onClickAddPresentation: () -> Unit,
) {
    HomeBottomSheetScaffold(
        maxHeight = maxHeight,
        headerHeight = headerHeight,
        sheetContent = {
            Content(contentPadding = PaddingValues(vertical = PrezelTheme.spacing.V32, horizontal = PrezelTheme.spacing.V20)) {
                item { Title(title = stringResource(R.string.feature_home_impl_bottom_sheet_empty_title)) }
            }
        },
    ) {
        EmptyTitleSection(
            nickname = uiState.nickname,
            onClickAddPresentation = onClickAddPresentation,
        )
    }
}

@Composable
private fun EmptyTitleSection(
    nickname: String,
    onClickAddPresentation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeTitleSection(
        backgroundResId = R.drawable.feature_home_impl_section_title_empty,
        modifier = modifier,
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
        PracticeActionCard(
            title = stringResource(R.string.feature_home_impl_add_presentation_title),
            actionText = stringResource(R.string.feature_home_impl_add_presentation_action),
            titleColor = PrezelTheme.colors.interactiveRegular,
            modifier = Modifier.fillMaxWidth(),
            onClick = onClickAddPresentation,
        )
    }
}

@BasicPreview
@Composable
private fun EmptyTitleSectionPreview() {
    PrezelTheme {
        EmptyTitleSection(
            nickname = "프레즐",
            onClickAddPresentation = {},
        )
    }
}

@BasicPreview
@Composable
private fun EmptyPresentationContentPreview() {
    PrezelTheme {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            EmptyPresentationContent(
                uiState = HomeUiState.Empty(nickname = "프레즐"),
                maxHeight = maxHeight,
                headerHeight = 0.dp,
                onClickAddPresentation = {},
            )
        }
    }
}
