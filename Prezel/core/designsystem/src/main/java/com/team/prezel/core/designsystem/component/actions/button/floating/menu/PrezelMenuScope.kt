package com.team.prezel.core.designsystem.component.actions.button.floating.menu

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable

@LayoutScopeMarker
interface PrezelMenuScope {
    val menuSize: MenuSize
    val itemSize: MenuItemSize

    @Composable
    fun MenuItem(
        label: String,
        @DrawableRes iconResId: Int,
        onClick: () -> Unit,
    )
}

internal class DefaultPrezelMenuScope(
    override val menuSize: MenuSize,
) : PrezelMenuScope {
    override val itemSize: MenuItemSize = when (menuSize) {
        MenuSize.SMALL -> MenuItemSize.SMALL
        MenuSize.REGULAR -> MenuItemSize.REGULAR
    }

    @Composable
    override fun MenuItem(
        label: String,
        @DrawableRes iconResId: Int,
        onClick: () -> Unit,
    ) {
        PrezelMenuItem(
            label = label,
            iconResId = iconResId,
            size = itemSize,
            onClick = onClick,
        )
    }
}
