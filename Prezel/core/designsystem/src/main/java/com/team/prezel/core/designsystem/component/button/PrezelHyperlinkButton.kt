package com.team.prezel.core.designsystem.component.button

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelHyperlinkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: PrezelButtonSize = PrezelButtonSize.XSMALL,
) {
    PrezelButton(
        text = text,
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        style = PrezelButtonStyle(
            buttonType = PrezelButtonType.GHOST,
            buttonHierarchy = PrezelButtonHierarchy.SECONDARY,
            buttonSize = size,
            showUnderline = true,
        ),
    )
}

@ThemePreview
@Composable
private fun PrezelHyperlinkButtonPreview() {
    PrezelTheme {
        PrezelHyperlinkButton(
            text = "자세히 보기",
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
