package com.team.prezel.core.designsystem.component.actions.button

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
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.designsystem.util.drawDashBorder

/**
 * 텍스트 링크처럼 보이는 보조 액션 버튼입니다.
 */
@Composable
fun PrezelHyperlinkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
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
        enabled = true,
        onClick = onClick,
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

@BasicPreview
@Composable
private fun PrezelHyperlinkButtonPreview() {
    PreviewSection(
        title = "Hyperlink Button",
        description = "텍스트 링크처럼 보이는 보조 액션 버튼입니다.",
    ) {
        PrezelHyperlinkButton(
            text = "자세히 보기",
            onClick = {},
            modifier = Modifier.drawDashBorder(),
        )
    }
}
