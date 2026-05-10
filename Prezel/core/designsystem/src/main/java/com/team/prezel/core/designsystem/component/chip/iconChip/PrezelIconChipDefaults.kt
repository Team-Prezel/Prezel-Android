package com.team.prezel.core.designsystem.component.chip.iconChip

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.chip.base.PrezelChipColors
import com.team.prezel.core.designsystem.component.chip.base.PrezelChipStyle
import com.team.prezel.core.designsystem.component.chip.base.iconChipShape
import com.team.prezel.core.designsystem.component.chip.base.iconChipTextStyle
import com.team.prezel.core.designsystem.theme.PrezelTheme

object PrezelIconChipDefaults {
    @Composable
    internal fun getDefault(
        type: IconChipType,
        size: IconChipSize,
        state: IconChipState,
        status: IconChipStatus,
    ): PrezelChipStyle =
        PrezelChipStyle(
            shape = iconChipShape(size = size),
            textStyle = iconChipTextStyle(size = size),
            colors = iconChipColors(type = type, state = state, status = status),
            contentPadding = iconContentPadding(size = size),
            iconTextSpacing = 0.dp,
            iconSize = iconSize(size = size),
        )

    @Composable
    private fun iconSize(size: IconChipSize): Dp =
        when (size) {
            IconChipSize.SMALL -> 12.dp
            IconChipSize.REGULAR -> 14.dp
        }

    @Composable
    private fun iconChipColors(
        type: IconChipType,
        state: IconChipState,
        status: IconChipStatus,
    ): PrezelChipColors {
        val contentAccentColor = resolveIconChipStatusContentColor(status = status)
        val iconColor = resolveIconChipContentColor(
            type = type,
            state = state,
            contentAccentColor = contentAccentColor,
        )

        return PrezelChipColors(
            containerColor = resolveIconChipContainerColor(
                type = type,
                state = state,
                status = status,
            ),
            iconColor = iconColor,
            textColor = iconColor,
            borderColor = if (type == IconChipType.OUTLINED) {
                resolveIconChipBorderColor(
                    state = state,
                    contentAccentColor = contentAccentColor,
                )
            } else {
                null
            },
        )
    }

    @Composable
    private fun iconContentPadding(size: IconChipSize): PaddingValues {
        val all = when (size) {
            IconChipSize.SMALL -> PrezelTheme.spacing.V6
            IconChipSize.REGULAR -> PrezelTheme.spacing.V8
        }

        return PaddingValues(all = all)
    }

    @Composable
    private fun resolveIconChipContainerColor(
        type: IconChipType,
        state: IconChipState,
        status: IconChipStatus,
    ): Color {
        if (state == IconChipState.DISABLED) return PrezelTheme.colors.bgMedium
        if (type == IconChipType.OUTLINED) return Color.Transparent

        return when (state) {
            IconChipState.ACTIVE -> PrezelTheme.colors.interactiveXSmall
            IconChipState.DEFAULT -> {
                when (status) {
                    IconChipStatus.DEFAULT -> PrezelTheme.colors.bgLarge
                    IconChipStatus.BAD -> PrezelTheme.colors.feedbackBadSmall
                }
            }

            IconChipState.DISABLED -> error("Handled above")
        }
    }

    @Composable
    private fun resolveIconChipContentColor(
        type: IconChipType,
        state: IconChipState,
        contentAccentColor: Color?,
    ): Color {
        if (state == IconChipState.DISABLED) return PrezelTheme.colors.iconDisabled
        if (contentAccentColor != null) return contentAccentColor
        if (state == IconChipState.ACTIVE) return PrezelTheme.colors.interactiveRegular
        if (type == IconChipType.FILLED) return PrezelTheme.colors.iconMedium

        return PrezelTheme.colors.iconRegular
    }

    @Composable
    private fun resolveIconChipBorderColor(
        state: IconChipState,
        contentAccentColor: Color?,
    ): Color {
        if (state == IconChipState.DISABLED) return PrezelTheme.colors.borderRegular
        if (contentAccentColor != null) return contentAccentColor
        if (state == IconChipState.ACTIVE) return PrezelTheme.colors.interactiveRegular

        return PrezelTheme.colors.borderMedium
    }

    @Composable
    private fun resolveIconChipStatusContentColor(status: IconChipStatus): Color? =
        when (status) {
            IconChipStatus.DEFAULT -> null
            IconChipStatus.BAD -> PrezelTheme.colors.feedbackBadRegular
        }
}
