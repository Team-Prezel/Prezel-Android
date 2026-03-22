package com.team.prezel.core.designsystem.component.actions.button

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonBase
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefault
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonPreviewContent
import com.team.prezel.core.designsystem.component.actions.button.config.previewGhostBorder
import com.team.prezel.core.designsystem.icon.PrezelIcons

@Composable
fun PrezelButton(
    text: String,
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.FILLED,
    size: ButtonSize = ButtonSize.REGULAR,
    hierarchy: ButtonHierarchy = ButtonHierarchy.PRIMARY,
    enabled: Boolean = true,
    isRounded: Boolean = false,
    buttonDefault: PrezelButtonDefault? = null,
    onClick: () -> Unit,
) {
    PrezelButtonBase(
        text = text,
        iconResId = iconResId,
        modifier = modifier,
        onClick = onClick,
        buttonDefault = buttonDefault ?: PrezelButtonDefaults.getDefault(
            isIconOnly = false,
            isRounded = isRounded,
            type = type,
            size = size,
            hierarchy = hierarchy,
            enabled = enabled,
        ),
    )
}

@Preview(device = "spec:width=1080dp,height=1400dp")
@Composable
private fun PrezelButtonPreview() {
    PrezelButtonPreviewContent(title = "Button/Icon + Text") { type, hierarchy, size, enabled, isRounded ->
        PrezelButton(
            text = "Label",
            iconResId = PrezelIcons.Blank,
            type = type,
            size = size,
            hierarchy = hierarchy,
            enabled = enabled,
            isRounded = isRounded,
            onClick = {},
            modifier = Modifier.previewGhostBorder(
                type = type,
                hierarchy = hierarchy,
                size = size,
                enabled = enabled,
                isRounded = isRounded,
                isIconOnly = false,
            ),
        )
    }
}
