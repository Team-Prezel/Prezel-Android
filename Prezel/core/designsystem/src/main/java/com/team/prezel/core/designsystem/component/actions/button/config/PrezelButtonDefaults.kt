package com.team.prezel.core.designsystem.component.actions.button.config

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.theme.PrezelColorScheme
import com.team.prezel.core.designsystem.theme.PrezelTheme

/**
 * 버튼 컴포넌트가 공통으로 사용하는 시각 속성 묶음입니다.
 */
@Immutable
data class PrezelButtonDefault(
    private val contentColor: Color,
    private val disabledContentColor: Color,
    private val backgroundColor: Color,
    private val disabledBackgroundColor: Color,
    private val borderColor: Color,
    private val disabledBorderColor: Color,
    val borderWidth: Dp,
    val shape: RoundedCornerShape,
    val textStyle: TextStyle,
    val contentPadding: PaddingValues,
    val iconSpacing: Dp,
    val iconSize: Dp,
) {
    val hasBorder: Boolean = borderWidth > 0.dp

    fun contentColor(enabled: Boolean): Color = if (enabled) contentColor else disabledContentColor

    fun backgroundColor(enabled: Boolean): Color = if (enabled) backgroundColor else disabledBackgroundColor

    fun borderColor(enabled: Boolean): Color = if (enabled) borderColor else disabledBorderColor
}

/**
 * 버튼 타입과 크기에 맞는 기본 스타일 값을 제공합니다.
 */
object PrezelButtonDefaults {
    /**
     * 버튼 종류와 사용 형태에 맞는 기본 스타일을 계산합니다.
     *
     * 커스텀 `config`를 만들 때 기준값으로 사용할 수 있습니다.
     */
    @Composable
    fun getDefault(
        isIconOnly: Boolean,
        type: ButtonType,
        size: ButtonSize,
        hierarchy: ButtonHierarchy,
        isRounded: Boolean,
        contentColor: Color = getContentColor(type = type, hierarchy = hierarchy),
        disabledContentColor: Color = getDisabledContentColor(),
        backgroundColor: Color = getBackgroundColor(type = type, hierarchy = hierarchy),
        disabledBackgroundColor: Color = getDisabledBackgroundColor(type = type),
        borderColor: Color = getBorderColor(type = type, hierarchy = hierarchy),
        disabledBorderColor: Color = getDisabledBorderColor(type = type),
        borderWidth: Dp = getBorderWidth(type = type),
        shape: RoundedCornerShape = getShape(isRounded = isRounded, isIconOnly = isIconOnly, size = size),
        textStyle: TextStyle = getTextStyle(size = size),
        contentPadding: PaddingValues = getContentPadding(size = size, isIconOnly = isIconOnly),
        iconSpacing: Dp = getIconSpacing(size = size),
        iconSize: Dp = getIconSize(size = size),
    ) = PrezelButtonDefault(
        contentColor = contentColor,
        disabledContentColor = disabledContentColor,
        backgroundColor = backgroundColor,
        disabledBackgroundColor = disabledBackgroundColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor,
        borderWidth = borderWidth,
        shape = shape,
        textStyle = textStyle,
        contentPadding = contentPadding,
        iconSpacing = iconSpacing,
        iconSize = iconSize,
    )

    @Composable
    private fun getContentColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
    ): Color {
        if (hierarchy == ButtonHierarchy.SECONDARY) return PrezelTheme.colors.textMedium

        return when (type) {
            ButtonType.FILLED -> PrezelColorScheme.Dark.textLarge
            ButtonType.OUTLINED,
            ButtonType.GHOST,
            -> PrezelTheme.colors.interactiveRegular
        }
    }

    @Composable
    private fun getDisabledContentColor(): Color = PrezelTheme.colors.textDisabled

    @Composable
    private fun getBackgroundColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
    ): Color =
        when (type) {
            ButtonType.OUTLINED,
            ButtonType.GHOST,
            -> Color.Transparent

            ButtonType.FILLED -> {
                if (hierarchy == ButtonHierarchy.SECONDARY) PrezelTheme.colors.bgLarge else PrezelTheme.colors.interactiveRegular
            }
        }

    @Composable
    private fun getDisabledBackgroundColor(type: ButtonType): Color =
        when (type) {
            ButtonType.OUTLINED,
            ButtonType.GHOST,
            -> Color.Transparent

            ButtonType.FILLED -> PrezelTheme.colors.bgLarge
        }

    @Composable
    private fun getBorderColor(
        type: ButtonType,
        hierarchy: ButtonHierarchy,
    ): Color {
        if (type != ButtonType.OUTLINED) return Color.Transparent

        return when (hierarchy) {
            ButtonHierarchy.PRIMARY -> PrezelTheme.colors.interactiveRegular
            ButtonHierarchy.SECONDARY -> PrezelTheme.colors.borderMedium
        }
    }

    @Composable
    private fun getDisabledBorderColor(type: ButtonType): Color {
        if (type != ButtonType.OUTLINED) return Color.Transparent

        return PrezelTheme.colors.borderDisabled
    }

    @Composable
    private fun getBorderWidth(type: ButtonType): Dp = if (type == ButtonType.OUTLINED) PrezelTheme.stroke.V1 else 0.dp

    @Composable
    private fun getShape(
        isRounded: Boolean,
        isIconOnly: Boolean,
        size: ButtonSize,
    ): RoundedCornerShape {
        if (isRounded) return PrezelTheme.shapes.V1000

        return when (size) {
            ButtonSize.REGULAR -> PrezelTheme.shapes.V8
            ButtonSize.SMALL -> if (isIconOnly) PrezelTheme.shapes.V6 else PrezelTheme.shapes.V4
            ButtonSize.XSMALL -> PrezelTheme.shapes.V4
        }
    }

    @Composable
    private fun getTextStyle(size: ButtonSize): TextStyle =
        when (size) {
            ButtonSize.XSMALL -> PrezelTheme.typography.caption2Medium
            ButtonSize.SMALL -> PrezelTheme.typography.body3Medium
            ButtonSize.REGULAR -> PrezelTheme.typography.body2Bold
        }

    @Composable
    private fun getContentPadding(
        size: ButtonSize,
        isIconOnly: Boolean,
    ): PaddingValues {
        if (isIconOnly) {
            val all = when (size) {
                ButtonSize.XSMALL -> PrezelTheme.spacing.V8
                ButtonSize.SMALL -> PrezelTheme.spacing.V10
                ButtonSize.REGULAR -> PrezelTheme.spacing.V14
            }

            return PaddingValues(all = all)
        }

        val horizontal = when (size) {
            ButtonSize.XSMALL -> PrezelTheme.spacing.V10
            ButtonSize.SMALL -> PrezelTheme.spacing.V12
            ButtonSize.REGULAR -> PrezelTheme.spacing.V16
        }
        val vertical = when (size) {
            ButtonSize.XSMALL -> PrezelTheme.spacing.V6
            ButtonSize.SMALL -> PrezelTheme.spacing.V8
            ButtonSize.REGULAR -> PrezelTheme.spacing.V12
        }

        return PaddingValues(horizontal = horizontal, vertical = vertical)
    }

    @Composable
    private fun getIconSpacing(size: ButtonSize): Dp =
        when (size) {
            ButtonSize.XSMALL,
            ButtonSize.SMALL,
            -> PrezelTheme.spacing.V4

            ButtonSize.REGULAR -> PrezelTheme.spacing.V8
        }

    private fun getIconSize(size: ButtonSize): Dp =
        when (size) {
            ButtonSize.XSMALL -> 14.dp
            ButtonSize.SMALL -> 16.dp
            ButtonSize.REGULAR -> 20.dp
        }
}
