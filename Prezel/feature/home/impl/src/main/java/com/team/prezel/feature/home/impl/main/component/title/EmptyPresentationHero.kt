package com.team.prezel.feature.home.impl.main.component.title

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.home.impl.R

@Composable
internal fun EmptyPresentationHero(
    nickname: String,
    onClickAddPresentation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeHeroLayout(
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
        EmptyPresentationHero(
            nickname = "프레즐",
            onClickAddPresentation = {},
        )
    }
}
