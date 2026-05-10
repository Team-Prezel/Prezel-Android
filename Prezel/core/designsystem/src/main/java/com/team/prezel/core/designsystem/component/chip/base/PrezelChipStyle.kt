package com.team.prezel.core.designsystem.component.chip.base

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.team.prezel.core.designsystem.component.chip.chip.ChipAccent
import com.team.prezel.core.designsystem.component.chip.chip.ChipSize
import com.team.prezel.core.designsystem.component.chip.chip.ChipStatus
import com.team.prezel.core.designsystem.component.chip.iconChip.IconChipSize
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

@Composable
internal fun chipShape(size: ChipSize): Shape =
    when (size) {
        ChipSize.SMALL -> PrezelTheme.shapes.V4
        ChipSize.REGULAR -> PrezelTheme.shapes.V8
    }

@Composable
internal fun chipTextStyle(size: ChipSize): TextStyle =
    when (size) {
        ChipSize.SMALL -> PrezelTheme.typography.caption2Regular
        ChipSize.REGULAR -> PrezelTheme.typography.caption1Regular
    }

@Composable
internal fun iconChipShape(size: IconChipSize): Shape =
    when (size) {
        IconChipSize.SMALL -> PrezelTheme.shapes.V4
        IconChipSize.REGULAR -> PrezelTheme.shapes.V8
    }

@Composable
internal fun iconChipTextStyle(size: IconChipSize): TextStyle =
    when (size) {
        IconChipSize.SMALL -> PrezelTheme.typography.caption2Regular
        IconChipSize.REGULAR -> PrezelTheme.typography.caption1Regular
    }

@Composable
internal fun resolveContainerAccent(
    status: ChipStatus,
    accent: ChipAccent,
): Color? =
    when (accent) {
        ChipAccent.WARNING -> PrezelTheme.colors.feedbackWarningSmall
        ChipAccent.PURPLE -> PrezelTheme.colors.accentPurpleSmall
        ChipAccent.TEAL -> PrezelTheme.colors.accentTealSmall
        ChipAccent.DEFAULT -> {
            when (status) {
                ChipStatus.DEFAULT -> null
                ChipStatus.BAD -> PrezelTheme.colors.feedbackBadSmall
            }
        }
    }

@Composable
internal fun resolveContentAccent(
    status: ChipStatus,
    accent: ChipAccent,
): Color? =
    when (accent) {
        ChipAccent.WARNING -> PrezelTheme.colors.feedbackWarningRegular
        ChipAccent.PURPLE -> PrezelTheme.colors.accentPurpleRegular
        ChipAccent.TEAL -> PrezelTheme.colors.accentTealRegular
        ChipAccent.DEFAULT -> {
            when (status) {
                ChipStatus.DEFAULT -> null
                ChipStatus.BAD -> PrezelTheme.colors.feedbackBadRegular
            }
        }
    }
