package com.team.prezel.feature.history.impl.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.EmptyView
import com.team.prezel.feature.history.impl.R

@Composable
internal fun HistoryEmptyContent(
    isPreparingTab: Boolean,
    onClickAddPresentation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EmptyView(
        title = stringResource(
            if (isPreparingTab) {
                R.string.feature_history_impl_empty_preparing
            } else {
                R.string.feature_history_impl_empty_completed
            },
        ),
        modifier = modifier,
        image = {
            Image(
                painter = painterResource(R.drawable.feature_history_impl_empty_no_presentation),
                modifier = Modifier.size(120.dp),
                contentDescription = null,
            )
        },
        action = if (isPreparingTab) {
            {
                PrezelButton(
                    text = stringResource(R.string.feature_history_impl_add_presentation),
                    type = ButtonType.OUTLINED,
                    size = ButtonSize.SMALL,
                    isRounded = true,
                    onClick = onClickAddPresentation,
                )
            }
        } else {
            null
        },
    )
}

@BasicPreview
@Composable
private fun HistoryPreparingEmptyContentPreview() {
    PrezelTheme {
        HistoryEmptyContent(
            isPreparingTab = true,
            onClickAddPresentation = { },
        )
    }
}

@BasicPreview
@Composable
private fun HistoryCompletedEmptyContentPreview() {
    PrezelTheme {
        HistoryEmptyContent(
            isPreparingTab = false,
            onClickAddPresentation = { },
        )
    }
}
