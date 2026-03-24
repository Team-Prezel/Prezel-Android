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

/**
 * 아이콘만 노출하는 액션 버튼입니다.
 */
@Composable
fun PrezelIconButton(
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.FILLED,
    size: ButtonSize = ButtonSize.REGULAR,
    hierarchy: ButtonHierarchy = ButtonHierarchy.PRIMARY,
    enabled: Boolean = true,
    isRounded: Boolean = false,
    buttonDefault: PrezelButtonDefault = PrezelButtonDefaults.getDefault(
        isIconOnly = true,
        isRounded = isRounded,
        type = type,
        size = size,
        hierarchy = hierarchy,
    ),
    onClick: () -> Unit,
) {
    PrezelButtonBase(
        text = null,
        iconResId = iconResId,
        enabled = enabled,
        onClick = onClick,
        modifier = modifier,
        config = buttonDefault,
    )
}

@Preview(device = "spec:width=1080dp,height=1500dp")
@Composable
private fun PrezelIconButtonPreview() {
    PrezelButtonPreviewContent(title = "Button/Icon") { type, hierarchy, size, enabled, isRounded ->
        PrezelIconButton(
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
                isRounded = isRounded,
                isIconOnly = true,
            ),
        )
    }
}
