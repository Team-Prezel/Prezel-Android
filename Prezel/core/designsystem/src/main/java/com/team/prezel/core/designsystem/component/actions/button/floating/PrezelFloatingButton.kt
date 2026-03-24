package com.team.prezel.core.designsystem.component.actions.button.floating

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelIconButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelFloatingButton(
    isExpanded: Boolean,
    onChangeExpanded: (Boolean) -> Unit,
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    size: ButtonSize = ButtonSize.REGULAR,
    hierarchy: ButtonHierarchy = ButtonHierarchy.PRIMARY,
    @DrawableRes openIconResId: Int = PrezelIcons.Cancel,
) {
    PrezelIconButton(
        iconResId = if (isExpanded) openIconResId else iconResId,
        size = size,
        hierarchy = hierarchy,
        isRounded = true,
        modifier = modifier,
        onClick = { onChangeExpanded(!isExpanded) },
    )
}

@ThemePreview
@Composable
private fun PrezelFloatingButtonPreview() {
    var expanded by remember { mutableStateOf(false) }

    PrezelTheme {
        Column(
            modifier = Modifier
                .background(PrezelTheme.colors.bgRegular)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PrezelFloatingButton(
                isExpanded = expanded,
                onChangeExpanded = { expanded = it },
                iconResId = PrezelIcons.Blank,
                hierarchy = ButtonHierarchy.PRIMARY,
                size = ButtonSize.REGULAR,
            )

            PrezelFloatingButton(
                isExpanded = expanded,
                onChangeExpanded = { expanded = it },
                iconResId = PrezelIcons.Blank,
                hierarchy = ButtonHierarchy.SECONDARY,
                size = ButtonSize.REGULAR,
            )

            PrezelFloatingButton(
                isExpanded = expanded,
                onChangeExpanded = { expanded = it },
                iconResId = PrezelIcons.Blank,
                hierarchy = ButtonHierarchy.PRIMARY,
                size = ButtonSize.SMALL,
            )

            PrezelFloatingButton(
                isExpanded = expanded,
                onChangeExpanded = { expanded = it },
                iconResId = PrezelIcons.Blank,
                hierarchy = ButtonHierarchy.SECONDARY,
                size = ButtonSize.SMALL,
            )
        }
    }
}
