package com.team.prezel.core.designsystem.component.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.team.prezel.core.designsystem.icon.DrawableIcon
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelChip(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: IconSource? = null,
    style: PrezelChipStyle = PrezelChipStyle(),
) {
    val hasText = text != null
    val hasIcon = icon != null
    val iconOnly = hasIcon && !hasText
    require(hasText || hasIcon) { "Chip은 text 또는 icon 중 하나는 반드시 필요합니다." }
    val (_, chipSize, _, _) = style

    Surface(
        modifier = modifier,
        shape = style.shape(),
        color = style.containerColor(iconOnly = iconOnly),
        border = style.borderStroke(),
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides style.textStyle(),
            LocalContentColor provides style.contentColor(),
        ) {
            Row(
                modifier = Modifier.padding(style.contentPadding(iconOnly = iconOnly)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PrezelChipIcon(icon = icon, style = style)

                if (!hasText) return@Row
                if (hasIcon) {
                    val spacing = if (chipSize == PrezelChipSize.REGULAR) PrezelTheme.spacing.V4 else PrezelTheme.spacing.V2
                    Spacer(modifier = Modifier.width(width = spacing))
                }

                Text(text = text)
            }
        }
    }
}

@Composable
fun PrezelChip(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: IconSource? = null,
    style: PrezelChipStyle = PrezelChipStyle(),
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
) {
    val hasText = text != null
    val hasIcon = icon != null
    val iconOnly = hasIcon && !hasText
    require(hasText || hasIcon) { "Chip은 text 또는 icon 중 하나는 반드시 필요합니다." }

    val colors = PrezelChipColors(
        containerColor = containerColor,
        contentColor = contentColor,
    )

    CompositionLocalProvider(LocalPrezelChipColors provides colors) {
        val resolvedContainer = style.containerColor(iconOnly = iconOnly)
        val resolvedContent = style.contentColor()
        val resolvedBorder = style.borderStroke()

        Surface(
            modifier = modifier,
            shape = style.shape(),
            color = resolvedContainer,
            border = resolvedBorder,
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides style.textStyle(),
                LocalContentColor provides resolvedContent,
            ) {
                Row(
                    modifier = Modifier.padding(style.contentPadding(iconOnly = iconOnly)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PrezelChipIcon(icon = icon, style = style)

                    if (hasText) {
                        if (hasIcon) {
                            val spacing = when (style.size) {
                                PrezelChipSize.REGULAR -> PrezelTheme.spacing.V4
                                PrezelChipSize.SMALL -> PrezelTheme.spacing.V2
                            }
                            Spacer(modifier = Modifier.width(spacing))
                        }
                        Text(text = text)
                    }
                }
            }
        }
    }
}

@Composable
private fun PrezelChipIcon(
    icon: IconSource?,
    style: PrezelChipStyle,
    modifier: Modifier = Modifier,
) {
    if (icon == null) return

    Icon(
        painter = icon.painter(),
        contentDescription = icon.contentDescription(),
        modifier = modifier.size(style.iconSize()),
    )
}

@ThemePreview
@Composable
private fun PrezelChipPreview() {
    PrezelTheme {
        PrezelTheme {
            PreviewScaffold {
                PrezelChipPreviewByType(
                    type = PrezelChipType.FILLED,
                ) { style -> PrezelChipPreviewItem(style) }

                HorizontalDivider()

                PrezelChipPreviewByType(
                    type = PrezelChipType.OUTLINED,
                ) { style -> PrezelChipPreviewItem(style) }
            }
        }
    }
}

@Composable
private fun PrezelChipPreviewItem(style: PrezelChipStyle) {
    PrezelChip(
        text = "Label",
        icon = DrawableIcon(resId = PrezelIcons.Blank),
        style = style,
    )
}
