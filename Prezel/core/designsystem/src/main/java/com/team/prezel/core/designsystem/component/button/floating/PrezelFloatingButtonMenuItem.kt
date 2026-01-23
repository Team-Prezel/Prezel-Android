package com.team.prezel.core.designsystem.component.button.floating

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.DrawableIcon
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelFloatingButtonMenuItem(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSource: IconSource? = null,
    size: PrezelFloatingButtonMenuItemSize = PrezelFloatingButtonMenuItemSize.REGULAR,
) {
    Row(
        modifier = modifier
            .clip(PrezelTheme.shapes.V4)
            .clickable(
                indication = ripple(),
                interactionSource = null,
                onClick = onClick,
            ).padding(prezelFloatingButtonMenuItemPaddingValues(size)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        iconSource?.let { source -> PrezelFloatingButtonMenuItemIcon(iconSource = source, size = size) }
        Text(text = label, style = prezelFloatingButtonMenuItemTextStyle(size))
    }
}

@Composable
private fun PrezelFloatingButtonMenuItemIcon(
    iconSource: IconSource,
    size: PrezelFloatingButtonMenuItemSize,
) {
    Icon(
        painter = iconSource.painter(),
        contentDescription = iconSource.contentDescription,
        modifier = Modifier.size(prezelFloatingButtonMenuItemIconSize(size)),
    )

    when (size) {
        PrezelFloatingButtonMenuItemSize.SMALL -> PrezelTheme.spacing.V4
        PrezelFloatingButtonMenuItemSize.REGULAR -> PrezelTheme.spacing.V8
    }.let { spacing -> Spacer(modifier = Modifier.width(spacing)) }
}

@ThemePreview
@Composable
private fun PrezelFloatingButtonMenuItemPreview() {
    PrezelTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(PrezelTheme.colors.bgRegular)
                .padding(16.dp),
        ) {
            PrezelFloatingButtonMenuItem(
                label = "Label",
                onClick = {},
                iconSource = DrawableIcon(resId = PrezelIcons.Blank),
                size = PrezelFloatingButtonMenuItemSize.REGULAR,
            )

            PrezelFloatingButtonMenuItem(
                label = "Label",
                onClick = {},
                iconSource = DrawableIcon(resId = PrezelIcons.Blank),
                size = PrezelFloatingButtonMenuItemSize.SMALL,
            )
            PrezelFloatingButtonMenuItem(
                label = "Label",
                onClick = {},
                size = PrezelFloatingButtonMenuItemSize.REGULAR,
            )

            PrezelFloatingButtonMenuItem(
                label = "Label",
                onClick = {},
                size = PrezelFloatingButtonMenuItemSize.SMALL,
            )
        }
    }
}
