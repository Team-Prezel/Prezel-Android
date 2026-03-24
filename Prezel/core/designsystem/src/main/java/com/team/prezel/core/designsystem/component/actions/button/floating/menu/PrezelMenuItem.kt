package com.team.prezel.core.designsystem.component.actions.button.floating.menu

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.designsystem.util.drawDashBorder

/**
 * 메뉴 scope가 실제로 그리는 클릭 가능한 단일 메뉴 아이템입니다.
 */
@Composable
internal fun PrezelMenuItem(
    label: String,
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    size: MenuItemSize = MenuItemSize.REGULAR,
    config: PrezelMenuItemDefault = PrezelMenuItemDefaults.getDefault(size),
    onClick: () -> Unit,
) {
    PrezelTouchArea(
        modifier = modifier,
        onClick = onClick,
        shape = config.shape,
        extraTouchPadding = config.contentPadding,
    ) {
        PrezelMenuItemLayout(
            label = label,
            iconResId = iconResId,
            config = config,
        )
    }
}

@Composable
private fun PrezelMenuItemLayout(
    label: String,
    @DrawableRes iconResId: Int,
    config: PrezelMenuItemDefault,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(config.iconSize),
            tint = config.contentColor,
        )

        Spacer(modifier = Modifier.width(config.spacing))

        Text(
            text = label,
            style = config.textStyle,
            color = config.contentColor,
        )
    }
}

@BasicPreview
@Composable
private fun PrezelMenuMenuItemPreview() {
    PrezelTheme {
        Column(modifier = Modifier.padding(8.dp)) {
            PrezelMenu(
                size = MenuSize.REGULAR,
                modifier = Modifier.drawDashBorder(
                    shape = PrezelMenuDefaults.getDefault(size = MenuSize.REGULAR).shape,
                ),
            ) {
                MenuItem(
                    label = "Label",
                    iconResId = PrezelIcons.Blank,
                    onClick = {},
                )
            }

            PrezelMenu(
                size = MenuSize.SMALL,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .drawDashBorder(
                        shape = PrezelMenuDefaults.getDefault(size = MenuSize.SMALL).shape,
                    ),
            ) {
                MenuItem(
                    label = "Label",
                    iconResId = PrezelIcons.Blank,
                    onClick = {},
                )
            }
        }
    }
}
