package com.team.prezel.core.designsystem.component.actions.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonBase
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefault
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

/**
 * 텍스트 링크처럼 보이는 보조 액션 버튼입니다.
 */
@Composable
fun PrezelHyperlinkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ButtonSize = ButtonSize.XSMALL,
    config: PrezelButtonDefault = PrezelButtonDefaults.getDefault(
        isIconOnly = false,
        isRounded = false,
        size = size,
        type = ButtonType.GHOST,
        hierarchy = ButtonHierarchy.SECONDARY,
    ),
) {
    PrezelButtonBase(
        text = text,
        iconResId = null,
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        layoutModifier = Modifier.prezelButtonUnderline(),
        config = config,
    )
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
private fun PrezelHyperlinkButtonPreview() {
    PrezelTheme {
        Box(modifier = Modifier.padding(8.dp)) {
            PrezelHyperlinkButton(
                text = "자세히 보기",
                onClick = {},
            )
        }
    }
}
