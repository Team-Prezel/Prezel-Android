package com.team.prezel.feature.home.impl.main.component.body

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.home.impl.R

@Composable
internal fun EmptyPresentationSheet(modifier: Modifier = Modifier) {
    HomeBottomSheetContent(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = PrezelTheme.spacing.V32, horizontal = PrezelTheme.spacing.V20),
    ) {
        HomeBottomSheetTitle(title = stringResource(R.string.feature_home_impl_bottom_sheet_empty_title))
        Spacer(modifier = Modifier.height(12.dp))
        PrezelButton(
            text = stringResource(R.string.feature_home_impl_practice_recording_action),
            onClick = {},
        )
    }
}

@BasicPreview
@Composable
private fun EmptyPresentationContentPreview() {
    PrezelTheme {
        Box(
            modifier = Modifier
                .height(100.dp)
                .padding(top = 16.dp),
        ) {
            EmptyPresentationSheet()
        }
    }
}
