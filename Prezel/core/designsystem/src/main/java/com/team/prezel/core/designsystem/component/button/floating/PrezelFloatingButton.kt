package com.team.prezel.core.designsystem.component.button.floating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.DrawableIcon
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelFloatingButton(
    iconSource: IconSource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: PrezelFloatingButtonStyle = PrezelFloatingButtonStyle(),
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .applyPrezelFloatingButtonShadow()
            .size(prezelFloatingButtonSize(style.size)),
        shape = PrezelTheme.shapes.V1000,
        containerColor = prezelFloatingButtonContainerColor(style.hierarchy),
        contentColor = prezelFloatingButtonContentColor(style.hierarchy),
        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
    ) {
        Icon(
            painter = iconSource.painter(),
            contentDescription = iconSource.contentDescription(),
            modifier = Modifier.size(prezelFloatingButtonIconSize(style.size)),
        )
    }
}

@ThemePreview
@Composable
private fun PrezelFloatingButtonPreview() {
    PrezelTheme {
        Column(
            modifier = Modifier
                .background(PrezelTheme.colors.bgRegular)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PrezelFloatingButton(
                iconSource = DrawableIcon(resId = PrezelIcons.Blank),
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.PRIMARY, size = PrezelFloatingButtonSize.REGULAR),
                onClick = {},
            )

            PrezelFloatingButton(
                iconSource = DrawableIcon(resId = PrezelIcons.Blank),
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.PRIMARY, size = PrezelFloatingButtonSize.SMALL),
                onClick = {},
            )

            PrezelFloatingButton(
                iconSource = DrawableIcon(resId = PrezelIcons.Blank),
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.SECONDARY, size = PrezelFloatingButtonSize.REGULAR),
                onClick = {},
            )

            PrezelFloatingButton(
                iconSource = DrawableIcon(resId = PrezelIcons.Blank),
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.SECONDARY, size = PrezelFloatingButtonSize.SMALL),
                onClick = {},
            )
        }
    }
}
