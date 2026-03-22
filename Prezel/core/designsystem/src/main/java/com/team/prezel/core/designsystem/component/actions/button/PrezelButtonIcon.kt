package com.team.prezel.core.designsystem.component.actions.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
internal fun PrezelButtonIcon(
    @DrawableRes drawableRes: Int,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier,
        painter = painterResource(id = drawableRes),
        contentScale = ContentScale.FillHeight,
        colorFilter = ColorFilter.tint(color = tint),
        contentDescription = null,
    )
}
