package com.team.prezel.core.designsystem.component.chip.base

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun PrezelChipLayout(
    modifier: Modifier = Modifier,
    text: String?,
    @DrawableRes iconResId: Int?,
    style: PrezelChipStyle,
) {
    require(text != null || iconResId != null) { "Chip은 text 또는 icon 중 하나는 반드시 필요합니다." }

    Surface(
        modifier = modifier,
        shape = style.shape,
        color = style.colors.containerColor,
        border = style.colors.borderColor?.let { color ->
            BorderStroke(width = PrezelTheme.stroke.V1, color = color)
        },
    ) {
        Row(
            modifier = Modifier.padding(style.contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            iconResId?.let { resId ->
                PrezelChipIcon(
                    iconResId = resId,
                    iconSize = style.iconSize,
                    tint = style.colors.iconColor,
                )
            }

            if (text != null) {
                if (iconResId != null) Spacer(modifier = Modifier.width(style.iconTextSpacing))
                Text(
                    text = text,
                    color = style.colors.textColor,
                    style = style.textStyle,
                )
            }
        }
    }
}

@Composable
private fun PrezelChipIcon(
    @DrawableRes iconResId: Int,
    iconSize: Dp,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = iconResId),
        contentDescription = null,
        modifier = modifier.size(iconSize),
        tint = tint,
    )
}
