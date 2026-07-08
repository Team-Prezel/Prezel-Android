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
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.StatusView
import com.team.prezel.feature.history.impl.R
import com.team.prezel.feature.history.impl.model.HistoryPageType

@Composable
internal fun HistoryEmptyContent(
    type: HistoryPageType,
    onClickAddPresentation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatusView(
        title = stringResource(
            when (type) {
                HistoryPageType.PREPARING -> R.string.feature_history_impl_empty_preparing
                HistoryPageType.COMPLETED -> R.string.feature_history_impl_empty_completed
            },
        ),
        modifier = modifier,
        visual = {
            Image(
                painter = painterResource(R.drawable.feature_history_impl_empty_no_presentation),
                modifier = Modifier.size(120.dp),
                contentDescription = null,
            )
        },
        action = when (type) {
            HistoryPageType.PREPARING -> {
                {
                    PrezelButton(
                        text = stringResource(R.string.feature_history_impl_add_presentation),
                        iconResId = PrezelIcons.Plus,
                        type = ButtonType.OUTLINED,
                        size = ButtonSize.SMALL,
                        isRounded = true,
                        onClick = onClickAddPresentation,
                    )
                }
            }

            HistoryPageType.COMPLETED -> null
        },
    )
}

@BasicPreview
@Composable
private fun HistoryPreparingEmptyContentPreview() {
    PrezelTheme {
        HistoryEmptyContent(
            type = HistoryPageType.PREPARING,
            onClickAddPresentation = { },
        )
    }
}

@BasicPreview
@Composable
private fun HistoryCompletedEmptyContentPreview() {
    PrezelTheme {
        HistoryEmptyContent(
            type = HistoryPageType.COMPLETED,
            onClickAddPresentation = { },
        )
    }
}
