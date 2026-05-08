package com.team.prezel.core.designsystem.component.chip.chip

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.chip.base.PrezelChipColors
import com.team.prezel.core.designsystem.component.chip.base.PrezelChipStyle
import com.team.prezel.core.designsystem.component.chip.base.chipShape
import com.team.prezel.core.designsystem.component.chip.base.chipTextStyle
import com.team.prezel.core.designsystem.component.chip.base.resolveContainerAccent
import com.team.prezel.core.designsystem.component.chip.base.resolveContentAccent
import com.team.prezel.core.designsystem.theme.PrezelTheme

object PrezelChipDefaults {
    @Composable
    internal fun getDefault(
        type: ChipType,
        size: ChipSize,
        state: ChipState,
        status: ChipStatus,
        hierarchy: ChipHierarchy,
        accent: ChipAccent,
    ): PrezelChipStyle =
        PrezelChipStyle(
            shape = chipShape(size = size),
            textStyle = chipTextStyle(size = size),
            colors = chipColors(
                type = type,
                state = state,
                status = status,
                hierarchy = hierarchy,
                accent = accent,
            ),
            contentPadding = contentPadding(size = size),
            iconTextSpacing = iconTextSpacing(size = size),
            iconSize = iconSize(size = size),
        )

    @Composable
    private fun iconSize(size: ChipSize): Dp =
        when (size) {
            ChipSize.SMALL -> 14.dp
            ChipSize.REGULAR -> 16.dp
        }

    @Composable
    private fun chipColors(
        type: ChipType,
        state: ChipState,
        status: ChipStatus,
        hierarchy: ChipHierarchy,
        accent: ChipAccent,
    ): PrezelChipColors {
        val contentAccentColor = resolveContentAccent(status = status, accent = accent)
        val usesDefaultHierarchyStyle = state == ChipState.DEFAULT &&
            status == ChipStatus.DEFAULT &&
            accent == ChipAccent.DEFAULT

        return PrezelChipColors(
            containerColor = resolveChipContainerColor(
                type = type,
                state = state,
                status = status,
                hierarchy = hierarchy,
                accent = accent,
            ),
            iconColor = resolveChipIconColor(
                type = type,
                state = state,
                hierarchy = hierarchy,
                contentAccentColor = contentAccentColor,
                usesDefaultHierarchyStyle = usesDefaultHierarchyStyle,
            ),
            textColor = resolveChipTextColor(
                type = type,
                state = state,
                hierarchy = hierarchy,
                contentAccentColor = contentAccentColor,
                usesDefaultHierarchyStyle = usesDefaultHierarchyStyle,
            ),
            borderColor = if (type == ChipType.OUTLINED) {
                resolveChipBorderColor(
                    state = state,
                    hierarchy = hierarchy,
                    contentAccentColor = contentAccentColor,
                )
            } else {
                null
            },
        )
    }

    @Composable
    private fun contentPadding(size: ChipSize): PaddingValues {
        val horizontal = when (size) {
            ChipSize.SMALL -> PrezelTheme.spacing.V6
            ChipSize.REGULAR -> PrezelTheme.spacing.V8
        }

        val vertical = when (size) {
            ChipSize.SMALL -> PrezelTheme.spacing.V4
            ChipSize.REGULAR -> PrezelTheme.spacing.V6
        }

        return PaddingValues(horizontal = horizontal, vertical = vertical)
    }

    @Composable
    private fun iconTextSpacing(size: ChipSize): Dp =
        when (size) {
            ChipSize.SMALL -> PrezelTheme.spacing.V2
            ChipSize.REGULAR -> PrezelTheme.spacing.V4
        }

    @Composable
    private fun resolveChipBorderColor(
        state: ChipState,
        hierarchy: ChipHierarchy,
        contentAccentColor: Color?,
    ): Color {
        if (contentAccentColor != null) return contentAccentColor
        if (state == ChipState.ACTIVE) return PrezelTheme.colors.interactiveRegular

        return when (hierarchy) {
            ChipHierarchy.PRIMARY -> PrezelTheme.colors.borderMedium
            ChipHierarchy.SECONDARY -> PrezelTheme.colors.borderRegular
        }
    }

    @Composable
    private fun resolveChipContainerColor(
        type: ChipType,
        state: ChipState,
        status: ChipStatus,
        hierarchy: ChipHierarchy,
        accent: ChipAccent,
    ): Color {
        val containerAccentColor = resolveContainerAccent(status = status, accent = accent)
        if (containerAccentColor != null) return containerAccentColor

        if (state == ChipState.ACTIVE) return PrezelTheme.colors.interactiveXSmall
        if (type == ChipType.OUTLINED) return PrezelTheme.colors.bgRegular
        if (
            state != ChipState.DEFAULT ||
            status != ChipStatus.DEFAULT ||
            accent != ChipAccent.DEFAULT
        ) {
            return PrezelTheme.colors.bgLarge
        }

        return when (hierarchy) {
            ChipHierarchy.PRIMARY -> PrezelTheme.colors.bgLarge
            ChipHierarchy.SECONDARY -> PrezelTheme.colors.chipContainerSecondaryFilledDefault
        }
    }

    @Composable
    private fun resolveChipIconColor(
        type: ChipType,
        state: ChipState,
        hierarchy: ChipHierarchy,
        contentAccentColor: Color?,
        usesDefaultHierarchyStyle: Boolean,
    ): Color {
        if (contentAccentColor != null) return contentAccentColor
        if (state == ChipState.ACTIVE) return PrezelTheme.colors.interactiveRegular
        if (type == ChipType.FILLED && usesDefaultHierarchyStyle && hierarchy == ChipHierarchy.PRIMARY) {
            return PrezelTheme.colors.iconMedium
        }

        return PrezelTheme.colors.iconRegular
    }

    @Composable
    private fun resolveChipTextColor(
        type: ChipType,
        state: ChipState,
        hierarchy: ChipHierarchy,
        contentAccentColor: Color?,
        usesDefaultHierarchyStyle: Boolean,
    ): Color {
        if (contentAccentColor != null) return contentAccentColor
        if (state == ChipState.ACTIVE) return PrezelTheme.colors.interactiveRegular
        if (type == ChipType.FILLED && usesDefaultHierarchyStyle && hierarchy == ChipHierarchy.PRIMARY) {
            return PrezelTheme.colors.textMedium
        }

        return PrezelTheme.colors.textRegular
    }
}
