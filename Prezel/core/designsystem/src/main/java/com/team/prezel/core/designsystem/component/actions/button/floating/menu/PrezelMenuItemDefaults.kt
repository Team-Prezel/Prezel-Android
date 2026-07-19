package com.team.prezel.core.designsystem.component.actions.button.floating.menu

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.theme.PrezelTheme

/**
 * [PrezelMenuItem]의 크기 프리셋입니다.
 */
enum class MenuItemSize {
    SMALL,
    REGULAR,
}

/**
 * 메뉴 아이템의 아이콘 크기, 간격, 타이포그래피를 묶은 스타일 값입니다.
 */
@Immutable
data class PrezelMenuItemDefault(
    val iconSize: Dp,
    val contentPadding: PaddingValues,
    val spacing: Dp,
    val textStyle: TextStyle,
    val contentColor: Color,
    val shape: RoundedCornerShape,
    val height: Dp,
)

/**
 * 메뉴 아이템 크기에 맞는 기본 스타일 값을 제공합니다.
 */
object PrezelMenuItemDefaults {
    /** 메뉴 아이템 크기에 맞는 기본 스타일을 반환합니다. */
    @Composable
    fun getDefault(size: MenuItemSize): PrezelMenuItemDefault =
        PrezelMenuItemDefault(
            iconSize = getIconSize(size),
            contentPadding = getContentPadding(size),
            spacing = getSpacing(size),
            textStyle = getTextStyle(size),
            contentColor = getContentColor(),
            shape = getShape(),
            height = getHeight(size),
        )

    private fun getIconSize(size: MenuItemSize): Dp =
        when (size) {
            MenuItemSize.SMALL -> 16.dp
            MenuItemSize.REGULAR -> 20.dp
        }

    @Composable
    private fun getContentPadding(size: MenuItemSize): PaddingValues {
        // 피그마 상으로 아이콘 사이즈를 고려하지 않은 패딩값이 존재하여 높이를 고정으로 하고 가운데 배치하도록 함
//        val verticalPadding = when (size) {
//            MenuItemSize.SMALL -> PrezelTheme.spacing.V4
//            MenuItemSize.REGULAR -> PrezelTheme.spacing.V8
//        }

        val startPadding = when (size) {
            MenuItemSize.SMALL -> PrezelTheme.spacing.V8
            MenuItemSize.REGULAR -> PrezelTheme.spacing.V12
        }

        val endPadding = when (size) {
            MenuItemSize.SMALL -> PrezelTheme.spacing.V10
            MenuItemSize.REGULAR -> PrezelTheme.spacing.V16
        }

        return PaddingValues(
            start = startPadding,
            end = endPadding,
//            top = verticalPadding,
//            bottom = verticalPadding,
        )
    }

    @Composable
    private fun getSpacing(size: MenuItemSize): Dp =
        when (size) {
            MenuItemSize.SMALL -> PrezelTheme.spacing.V4
            MenuItemSize.REGULAR -> PrezelTheme.spacing.V8
        }

    @Composable
    private fun getTextStyle(size: MenuItemSize): TextStyle =
        when (size) {
            MenuItemSize.SMALL -> PrezelTheme.typography.body3Regular
            MenuItemSize.REGULAR -> PrezelTheme.typography.body2Regular
        }

    @Composable
    private fun getContentColor(): Color = PrezelTheme.colors.textMedium

    @Composable
    private fun getShape(): RoundedCornerShape = PrezelTheme.shapes.V4

    @Composable
    private fun getHeight(size: MenuItemSize): Dp =
        when (size) {
            MenuItemSize.SMALL -> 28.dp
            MenuItemSize.REGULAR -> 40.dp
        }
}
