package com.team.prezel.feature.home.impl.main.component.body

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelTextButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.home.impl.R

@Composable
internal fun EmptySheet(modifier: Modifier = Modifier) {
    HomeBottomSheetContent(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = PrezelTheme.spacing.V32, horizontal = PrezelTheme.spacing.V20),
    ) {
        HomeBottomSheetTitle(title = stringResource(R.string.feature_home_impl_empty_sheet_practice_card_title))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(136.dp)
                .clip(PrezelTheme.shapes.V6)
                .background(PrezelTheme.colors.bgMedium)
                .padding(horizontal = PrezelTheme.spacing.V16, vertical = PrezelTheme.spacing.V12),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.feature_home_impl_empty_sheet_practice_card_message),
                    style = PrezelTheme.typography.body3Regular,
                    color = PrezelTheme.colors.textDisabled,
                )
            }

            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))

            PrezelTextButton(
                text = stringResource(R.string.feature_home_impl_empty_sheet_practice_card_button_text),
                type = ButtonType.FILLED,
                hierarchy = ButtonHierarchy.PRIMARY,
                size = ButtonSize.SMALL,
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                onClick = {},
            )
        }
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))
    }
}

@BasicPreview
@Composable
private fun EmptySheetPreview() {
    PrezelTheme {
        EmptySheet()
    }
}
