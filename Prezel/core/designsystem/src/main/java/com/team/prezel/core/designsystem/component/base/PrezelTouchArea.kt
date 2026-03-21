package com.team.prezel.core.designsystem.component.base

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelTouchArea(
    modifier: Modifier = Modifier,
    extraTouchPadding: PaddingValues = PaddingValues(0.dp),
    shape: Shape = RectangleShape,
    enabled: Boolean = true,
    rippleColor: Color = Color.Unspecified,
    isUseRipple: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .clip(shape = shape)
            .clickable(
                enabled = enabled,
                onClick = onClick,
                indication = if (isUseRipple) ripple(color = rippleColor) else null,
                interactionSource = interactionSource,
            ).padding(extraTouchPadding),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@ThemePreview
@Composable
private fun PrezelTouchAreaPreview() {
    PrezelTheme {
        PrezelTouchArea(onClick = {}) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Cyan),
            )
        }
    }
}
