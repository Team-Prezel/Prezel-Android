package com.team.prezel.core.designsystem.component.actions.button.config

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea

/**
 * 버튼 계열 컴포넌트가 공통으로 사용하는 실제 렌더링 구현입니다.
 */
@Composable
internal fun PrezelButtonBase(
    text: String?,
    @DrawableRes iconResId: Int?,
    enabled: Boolean,
    onClick: () -> Unit,
    config: PrezelButtonDefault,
    modifier: Modifier = Modifier,
    layoutModifier: Modifier = Modifier,
    isUseRipple: Boolean = true,
) {
    PrezelTouchArea(
        modifier = modifier.buttonContainer(enabled = enabled, config = config),
        extraTouchPadding = config.contentPadding,
        onClick = onClick,
        enabled = enabled,
        shape = config.shape,
        isUseRipple = isUseRipple,
    ) {
        ButtonContentLayout(
            modifier = layoutModifier,
            horizontalArrangement = config.contentArrangement(
                hasText = text != null,
                hasIcon = iconResId != null,
            ),
            text = text?.let { buttonText ->
                {
                    ButtonLabel(
                        text = buttonText,
                        enabled = enabled,
                        config = config,
                    )
                }
            },
            leadingIcon = iconResId?.let { drawableRes ->
                {
                    ButtonLeadingIcon(
                        drawableRes = drawableRes,
                        enabled = enabled,
                        config = config,
                    )
                }
            },
        )
    }
}

@Composable
private fun ButtonContentLayout(
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    text: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon?.invoke()
            text?.invoke()
        }
    }
}

@Composable
private fun ButtonLabel(
    text: String,
    enabled: Boolean,
    config: PrezelButtonDefault,
) {
    Text(
        text = text,
        style = config.textStyle,
        color = config.contentColor(enabled = enabled),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun ButtonLeadingIcon(
    @DrawableRes drawableRes: Int,
    enabled: Boolean,
    config: PrezelButtonDefault,
) {
    Image(
        modifier = Modifier.size(config.iconSize),
        painter = painterResource(id = drawableRes),
        contentScale = ContentScale.FillHeight,
        colorFilter = ColorFilter.tint(color = config.contentColor(enabled = enabled)),
        contentDescription = null,
    )
}

private fun PrezelButtonDefault.contentArrangement(
    hasText: Boolean,
    hasIcon: Boolean,
): Arrangement.Horizontal = if (hasText && hasIcon) Arrangement.spacedBy(iconSpacing) else Arrangement.Center

private fun Modifier.buttonContainer(
    enabled: Boolean,
    config: PrezelButtonDefault,
): Modifier =
    this
        .clip(shape = config.shape)
        .background(color = config.backgroundColor(enabled = enabled))
        .then(
            if (config.hasBorder) {
                Modifier.border(
                    width = config.borderWidth,
                    color = config.borderColor(enabled = enabled),
                    shape = config.shape,
                )
            } else {
                Modifier
            },
        )
