package com.team.prezel.core.designsystem.component.actions.button

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefault
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults

@Composable
fun PrezelIconButton(
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
        iconResId = iconResId,
        modifier = modifier,
        onClick = onClick,
        buttonDefault = buttonDefault ?: PrezelButtonDefaults.getDefault(
            isIconOnly = true,
            isRounded = isRounded,
            type = type,
            size = size,
            hierarchy = hierarchy,
            enabled = enabled,
        ),
    )
}
