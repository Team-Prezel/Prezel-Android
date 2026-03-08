package com.team.prezel.core.designsystem.component.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: IconSource? = null,
    enabled: Boolean = true,
    style: PrezelButtonStyle = PrezelButtonStyle(),
) {
    require(text != null || icon != null) { "버튼은 텍스트 또는 아이콘 중 하나는 반드시 필요합니다." }
    val appearance = PrezelButtonAppearance.of(style = style, isIconOnly = text == null, enabled = enabled)

    Box(
        modifier = modifier
            .applyButtonAppearance(appearance)
            .clickable(
                enabled = enabled,
                onClick = onClick,
                role = Role.Button,
                indication = ripple(),
                interactionSource = null,
            ),
    ) {
        PrezelButtonContent(
            text = text,
            icon = icon,
            showUnderline = style.showUnderline,
            buttonSize = style.buttonSize,
            appearance = appearance,
        )
    }
}

@Composable
private fun PrezelButtonContent(
    text: String?,
    icon: IconSource?,
    showUnderline: Boolean,
    buttonSize: PrezelButtonSize,
    appearance: PrezelButtonAppearance,
) {
    CompositionLocalProvider(LocalContentColor provides appearance.contentColor) {
        Row(
            modifier = Modifier.padding(appearance.contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let { source ->
                Icon(
                    painter = source.painter(),
                    contentDescription = source.contentDescription(),
                    modifier = Modifier.size(prezelButtonIconSize(size = buttonSize)),
                )
            }

            text?.let { label ->
                if (icon != null) {
                    Spacer(modifier = Modifier.width(width = appearance.iconSpacing))
                }
                Text(
                    text = label,
                    style = appearance.textStyle,
                    modifier = if (showUnderline) Modifier.prezelButtonUnderline() else Modifier,
                )
            }
        }
    }
}

@Composable
private fun Modifier.prezelButtonUnderline(): Modifier {
    val underlineThickness = with(LocalDensity.current) { 1.dp.toPx() }
    val underlineColor = PrezelTheme.colors.borderLarge

    return this.drawBehind {
        val y = size.height - (underlineThickness / 2)
        drawLine(
            color = underlineColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = underlineThickness,
        )
    }
}

@ThemePreview
@Composable
private fun PrezelButtonPreviewFilled() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.FILLED, content = ::PrezelButtonPreviewItem)
    }
}

@ThemePreview
@Composable
private fun PrezelButtonPreviewOutlined() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.OUTLINED, content = ::PrezelButtonPreviewItem)
    }
}

@ThemePreview
@Composable
private fun PrezelButtonPreviewGhost() {
    PrezelTheme {
        PrezelButtonPreviewByType(type = PrezelButtonType.GHOST, content = ::PrezelButtonPreviewItem)
    }
}

@Composable
private fun PrezelButtonPreviewItem(
    style: PrezelButtonStyle,
    enabled: Boolean,
) {
    PrezelButton(
        text = "Label",
        icon = IconSource(resId = PrezelIcons.Blank),
        onClick = {},
        enabled = enabled,
        style = style,
    )
}
