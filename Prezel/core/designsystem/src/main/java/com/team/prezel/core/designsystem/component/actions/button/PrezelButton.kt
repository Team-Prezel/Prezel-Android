package com.team.prezel.core.designsystem.component.actions.button

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonBase
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefault
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonPreviewContent
import com.team.prezel.core.designsystem.component.actions.button.config.previewGhostBorder
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.LargeDevicePreview

/**
 * 아이콘과 텍스트를 함께 표시할 수 있는 기본 버튼입니다.
 */
@Composable
fun PrezelButton(
    text: String,
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int? = null,
    type: ButtonType = ButtonType.FILLED,
    size: ButtonSize = ButtonSize.REGULAR,
    hierarchy: ButtonHierarchy = ButtonHierarchy.PRIMARY,
    enabled: Boolean = true,
    isRounded: Boolean = false,
    config: PrezelButtonDefault = PrezelButtonDefaults.getDefault(
        isIconOnly = false,
        isRounded = isRounded,
        type = type,
        size = size,
        hierarchy = hierarchy,
    ),
    onClick: () -> Unit,
) {
    PrezelButtonBase(
        text = text,
        iconResId = iconResId,
        enabled = enabled,
        onClick = onClick,
        modifier = modifier,
        config = config,
    )
}

@LargeDevicePreview
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
                isRounded = isRounded,
                isIconOnly = false,
            ),
        )
    }
}
