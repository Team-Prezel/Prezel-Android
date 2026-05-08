package com.team.prezel.core.designsystem.component.chip.config

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Immutable
internal data class PrezelChipColors(
    val containerColor: Color,
    val iconColor: Color,
    val textColor: Color,
    val borderColor: Color?,
)

@Immutable
internal data class PrezelChipStyle(
    val shape: Shape,
    val textStyle: TextStyle,
    val colors: PrezelChipColors,
    val contentPadding: PaddingValues,
    val iconTextSpacing: Dp,
    val iconSize: Dp,
)

object PrezelChipDefaults {
    @Composable
    internal fun chipStyle(
        type: PrezelChipType,
        size: PrezelChipSize,
        state: PrezelChipState,
        status: PrezelChipStatus,
        hierarchy: PrezelChipHierarchy,
        accent: PrezelChipAccent,
    ): PrezelChipStyle =
        PrezelChipStyle(
            shape = shape(size = size),
            textStyle = textStyle(size = size),
            colors = chipColors(
                type = type,
                state = normalizeChipState(state),
                status = status,
                hierarchy = hierarchy,
                accent = accent,
            ),
            contentPadding = contentPadding(size = size),
            iconTextSpacing = iconTextSpacing(size = size),
            iconSize = chipIconSize(size = size),
        )

    @Composable
    internal fun iconChipStyle(
        type: PrezelChipType,
        size: PrezelChipSize,
        state: PrezelChipState,
        status: PrezelChipStatus,
    ): PrezelChipStyle =
        PrezelChipStyle(
            shape = shape(size = size),
            textStyle = textStyle(size = size),
            colors = iconChipColors(type = type, state = state, status = status),
            contentPadding = iconContentPadding(size = size),
            iconTextSpacing = 0.dp,
            iconSize = iconChipIconSize(size = size),
        )

    @Composable
    private fun chipColors(
        type: PrezelChipType,
        state: PrezelChipState,
        status: PrezelChipStatus,
        hierarchy: PrezelChipHierarchy,
        accent: PrezelChipAccent,
    ): PrezelChipColors {
        val contentAccentColor = resolveContentAccent(status = status, accent = accent)
        val usesDefaultHierarchyStyle = usesHierarchyDefaultStyle(
            state = state,
            status = status,
            accent = accent,
        )

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
            borderColor = resolveOutlinedBorderColor(
                type = type,
                color = resolveChipBorderColor(
                    state = state,
                    hierarchy = hierarchy,
                    contentAccentColor = contentAccentColor,
                ),
            ),
        )
    }

    @Composable
    private fun iconChipColors(
        type: PrezelChipType,
        state: PrezelChipState,
        status: PrezelChipStatus,
    ): PrezelChipColors {
        val contentAccentColor = resolveContentAccent(
            status = status,
            accent = PrezelChipAccent.DEFAULT,
        )
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
            borderColor = resolveOutlinedBorderColor(
                type = type,
                color = resolveIconChipBorderColor(
                    state = state,
                    contentAccentColor = contentAccentColor,
                ),
            ),
        )
    }

    @Composable
    private fun shape(size: PrezelChipSize): Shape =
        when (size) {
            PrezelChipSize.SMALL -> PrezelTheme.shapes.V4
            PrezelChipSize.REGULAR -> PrezelTheme.shapes.V8
        }

    @Composable
    private fun textStyle(size: PrezelChipSize): TextStyle =
        when (size) {
            PrezelChipSize.SMALL -> PrezelTheme.typography.caption2Regular
            PrezelChipSize.REGULAR -> PrezelTheme.typography.caption1Regular
        }

    @Composable
    private fun contentPadding(size: PrezelChipSize): PaddingValues {
        val horizontal = when (size) {
            PrezelChipSize.SMALL -> PrezelTheme.spacing.V6
            PrezelChipSize.REGULAR -> PrezelTheme.spacing.V8
        }

        val vertical = when (size) {
            PrezelChipSize.SMALL -> PrezelTheme.spacing.V4
            PrezelChipSize.REGULAR -> PrezelTheme.spacing.V6
        }

        return PaddingValues(horizontal = horizontal, vertical = vertical)
    }

    @Composable
    private fun iconContentPadding(size: PrezelChipSize): PaddingValues {
        val all = when (size) {
            PrezelChipSize.SMALL -> PrezelTheme.spacing.V6
            PrezelChipSize.REGULAR -> PrezelTheme.spacing.V8
        }

        return PaddingValues(all = all)
    }

    @Composable
    private fun iconTextSpacing(size: PrezelChipSize): Dp =
        when (size) {
            PrezelChipSize.SMALL -> PrezelTheme.spacing.V2
            PrezelChipSize.REGULAR -> PrezelTheme.spacing.V4
        }

    private fun chipIconSize(size: PrezelChipSize): Dp =
        when (size) {
            PrezelChipSize.SMALL -> 14.dp
            PrezelChipSize.REGULAR -> 16.dp
        }

    private fun iconChipIconSize(size: PrezelChipSize): Dp =
        when (size) {
            PrezelChipSize.SMALL -> 12.dp
            PrezelChipSize.REGULAR -> 14.dp
        }

    @Composable
    private fun resolveChipBorderColor(
        state: PrezelChipState,
        hierarchy: PrezelChipHierarchy,
        contentAccentColor: Color?,
    ): Color {
        if (contentAccentColor != null) return contentAccentColor
        if (state == PrezelChipState.ACTIVE) return PrezelTheme.colors.interactiveRegular

        return when (hierarchy) {
            PrezelChipHierarchy.PRIMARY -> PrezelTheme.colors.borderMedium
            PrezelChipHierarchy.SECONDARY -> PrezelTheme.colors.borderRegular
        }
    }

    @Composable
    private fun resolveChipContainerColor(
        type: PrezelChipType,
        state: PrezelChipState,
        status: PrezelChipStatus,
        hierarchy: PrezelChipHierarchy,
        accent: PrezelChipAccent,
    ): Color {
        val containerAccentColor = resolveContainerAccent(status = status, accent = accent)
        if (containerAccentColor != null) return containerAccentColor

        if (state == PrezelChipState.ACTIVE) return PrezelTheme.colors.interactiveXSmall
        if (type == PrezelChipType.OUTLINED) return PrezelTheme.colors.bgRegular
        if (!usesHierarchyDefaultStyle(state = state, status = status, accent = accent)) {
            return PrezelTheme.colors.bgLarge
        }

        return when (hierarchy) {
            PrezelChipHierarchy.PRIMARY -> PrezelTheme.colors.bgLarge
            PrezelChipHierarchy.SECONDARY -> PrezelTheme.colors.chipContainerSecondaryFilledDefault
        }
    }

    @Composable
    private fun resolveChipIconColor(
        type: PrezelChipType,
        state: PrezelChipState,
        hierarchy: PrezelChipHierarchy,
        contentAccentColor: Color?,
        usesDefaultHierarchyStyle: Boolean,
    ): Color {
        if (contentAccentColor != null) return contentAccentColor
        if (state == PrezelChipState.ACTIVE) return PrezelTheme.colors.interactiveRegular
        if (type == PrezelChipType.FILLED && usesDefaultHierarchyStyle && hierarchy == PrezelChipHierarchy.PRIMARY) {
            return PrezelTheme.colors.iconMedium
        }

        return PrezelTheme.colors.iconRegular
    }

    @Composable
    private fun resolveChipTextColor(
        type: PrezelChipType,
        state: PrezelChipState,
        hierarchy: PrezelChipHierarchy,
        contentAccentColor: Color?,
        usesDefaultHierarchyStyle: Boolean,
    ): Color {
        if (contentAccentColor != null) return contentAccentColor
        if (state == PrezelChipState.ACTIVE) return PrezelTheme.colors.interactiveRegular
        if (type == PrezelChipType.FILLED && usesDefaultHierarchyStyle && hierarchy == PrezelChipHierarchy.PRIMARY) {
            return PrezelTheme.colors.textMedium
        }

        return PrezelTheme.colors.textRegular
    }

    private fun usesHierarchyDefaultStyle(
        state: PrezelChipState,
        status: PrezelChipStatus,
        accent: PrezelChipAccent,
    ): Boolean = state == PrezelChipState.DEFAULT && status == PrezelChipStatus.DEFAULT && accent == PrezelChipAccent.DEFAULT

    @Composable
    private fun resolveIconChipContainerColor(
        type: PrezelChipType,
        state: PrezelChipState,
        status: PrezelChipStatus,
    ): Color {
        if (type == PrezelChipType.OUTLINED) return Color.Transparent

        return when (state) {
            PrezelChipState.DISABLED -> PrezelTheme.colors.bgMedium
            PrezelChipState.ACTIVE -> PrezelTheme.colors.interactiveXSmall
            PrezelChipState.DEFAULT -> {
                when (status) {
                    PrezelChipStatus.DEFAULT -> PrezelTheme.colors.bgLarge
                    PrezelChipStatus.BAD -> PrezelTheme.colors.feedbackBadSmall
                    PrezelChipStatus.WARNING -> PrezelTheme.colors.feedbackWarningSmall
                }
            }
        }
    }

    @Composable
    private fun resolveIconChipContentColor(
        type: PrezelChipType,
        state: PrezelChipState,
        contentAccentColor: Color?,
    ): Color {
        if (state == PrezelChipState.DISABLED) return PrezelTheme.colors.iconDisabled
        if (contentAccentColor != null) return contentAccentColor
        if (state == PrezelChipState.ACTIVE) return PrezelTheme.colors.interactiveRegular
        if (type == PrezelChipType.FILLED) return PrezelTheme.colors.iconMedium

        return PrezelTheme.colors.iconRegular
    }

    @Composable
    private fun resolveIconChipBorderColor(
        state: PrezelChipState,
        contentAccentColor: Color?,
    ): Color {
        if (state == PrezelChipState.DISABLED) return PrezelTheme.colors.borderRegular
        if (contentAccentColor != null) return contentAccentColor
        if (state == PrezelChipState.ACTIVE) return PrezelTheme.colors.interactiveRegular

        return PrezelTheme.colors.borderMedium
    }

    private fun normalizeChipState(state: PrezelChipState): PrezelChipState =
        if (state == PrezelChipState.DISABLED) PrezelChipState.DEFAULT else state

    private fun resolveOutlinedBorderColor(
        type: PrezelChipType,
        color: Color,
    ): Color? = if (type == PrezelChipType.OUTLINED) color else null

    @Composable
    private fun resolveContainerAccent(
        status: PrezelChipStatus,
        accent: PrezelChipAccent,
    ): Color? =
        when (accent) {
            PrezelChipAccent.PURPLE -> PrezelTheme.colors.accentPurpleSmall
            PrezelChipAccent.TEAL -> PrezelTheme.colors.accentTealSmall
            PrezelChipAccent.DEFAULT -> {
                when (status) {
                    PrezelChipStatus.DEFAULT -> null
                    PrezelChipStatus.BAD -> PrezelTheme.colors.feedbackBadSmall
                    PrezelChipStatus.WARNING -> PrezelTheme.colors.feedbackWarningSmall
                }
            }
        }

    @Composable
    private fun resolveContentAccent(
        status: PrezelChipStatus,
        accent: PrezelChipAccent,
    ): Color? =
        when (accent) {
            PrezelChipAccent.PURPLE -> PrezelTheme.colors.accentPurpleRegular
            PrezelChipAccent.TEAL -> PrezelTheme.colors.accentTealRegular
            PrezelChipAccent.DEFAULT -> {
                when (status) {
                    PrezelChipStatus.DEFAULT -> null
                    PrezelChipStatus.BAD -> PrezelTheme.colors.feedbackBadRegular
                    PrezelChipStatus.WARNING -> PrezelTheme.colors.feedbackWarningRegular
                }
            }
        }
}
