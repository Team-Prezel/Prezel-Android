package com.team.prezel.core.designsystem.component.actions.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefault
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea

@Composable
internal fun PrezelButtonBase(
    onClick: () -> Unit,
    buttonDefault: PrezelButtonDefault,
    modifier: Modifier = Modifier,
    text: String? = null,
    @DrawableRes iconResId: Int? = null,
) {
    PrezelTouchArea(
        modifier = modifier,
        onClick = onClick,
        enabled = buttonDefault.enabled,
        shape = buttonDefault.shape,
        isUseRipple = true,
    ) {
        ButtonContentLayout(
            modifier = Modifier.buttonContent(buttonDefault),
            horizontalArrangement = buttonDefault.contentArrangement(
                hasText = text != null,
                hasIcon = iconResId != null,
            ),
            text = text?.let { buttonText ->
                {
                    ButtonLabel(
                        text = buttonText,
                        buttonDefault = buttonDefault,
                    )
                }
            },
            leadingIcon = iconResId?.let { drawableRes ->
                {
                    ButtonLeadingIcon(
                        drawableRes = drawableRes,
                        buttonDefault = buttonDefault,
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

            text?.let {
                Box(
                    modifier = Modifier.wrapContentHeight(),
                    contentAlignment = Alignment.Center,
                ) {
                    text()
                }
            }
        }
    }
}

@Composable
private fun ButtonLabel(
    text: String,
    buttonDefault: PrezelButtonDefault,
) {
    Text(
        text = text,
        style = buttonDefault.textStyle,
        color = buttonDefault.contentColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun ButtonLeadingIcon(
    @DrawableRes drawableRes: Int,
    buttonDefault: PrezelButtonDefault,
) {
    Image(
        modifier = Modifier.size(buttonDefault.iconSize),
        painter = painterResource(id = drawableRes),
        contentScale = ContentScale.FillHeight,
        colorFilter = ColorFilter.tint(color = buttonDefault.contentColor),
        contentDescription = null,
    )
}

private fun PrezelButtonDefault.contentArrangement(
    hasText: Boolean,
    hasIcon: Boolean,
): Arrangement.Horizontal = if (hasText && hasIcon) Arrangement.spacedBy(iconSpacing) else Arrangement.Center

private fun Modifier.buttonContent(buttonDefault: PrezelButtonDefault): Modifier =
    this
        .buttonContainer(buttonDefault)
        .padding(buttonDefault.contentPadding)

private fun Modifier.buttonContainer(buttonDefault: PrezelButtonDefault): Modifier =
    this
        .clip(shape = buttonDefault.shape)
        .background(color = buttonDefault.backgroundColor)
        .then(
            if (buttonDefault.hasBorder) {
                Modifier.border(
                    width = buttonDefault.borderWidth,
                    color = buttonDefault.borderColor,
                    shape = buttonDefault.shape,
                )
            } else {
                Modifier
            },
        )
