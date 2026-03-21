package com.team.prezel.core.designsystem.component.actions.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun PrezelButtonLayout(
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    text: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon?.let { leadingIcon() }

            text?.let {
                Box(
                    modifier = Modifier.wrapContentHeight(),
                    contentAlignment = Alignment.Center,
                    content = { text() },
                )
            }
        }
    }
}
