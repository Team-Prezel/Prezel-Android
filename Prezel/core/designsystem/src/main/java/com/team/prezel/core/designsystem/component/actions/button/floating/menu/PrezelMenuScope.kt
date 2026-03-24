package com.team.prezel.core.designsystem.component.actions.button.floating.menu

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable

/**
 * [PrezelMenu] 안에서 메뉴 항목을 선언할 때 사용하는 scope입니다.
 */
@LayoutScopeMarker
interface PrezelMenuScope {
    val menuSize: MenuSize
    val itemSize: MenuItemSize

    /** 현재 메뉴 크기에 맞는 기본 메뉴 아이템을 추가합니다. */
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
