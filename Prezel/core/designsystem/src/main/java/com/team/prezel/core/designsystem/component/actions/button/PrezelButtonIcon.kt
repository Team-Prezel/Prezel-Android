package com.team.prezel.core.designsystem.component.actions.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize

@Composable
internal fun PrezelButtonIcon(
    @DrawableRes drawableRes: Int,
    tint: Color,
    size: ButtonSize,
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier.size(
            when (size) {
                ButtonSize.XSMALL -> 14.dp
                ButtonSize.SMALL -> 16.dp
                ButtonSize.REGULAR -> 20.dp
            },
        ),
        painter = painterResource(id = drawableRes),
        contentScale = ContentScale.FillHeight,
        colorFilter = ColorFilter.tint(color = tint),
        contentDescription = null,
    )
}
