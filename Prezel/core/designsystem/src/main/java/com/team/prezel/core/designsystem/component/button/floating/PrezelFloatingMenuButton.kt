package com.team.prezel.core.designsystem.component.button.floating

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.icon.DrawableIcon
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelFloatingMenuButton(
    iconSource: IconSource,
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: PrezelFloatingButtonStyle = PrezelFloatingButtonStyle(),
    content: @Composable (ColumnScope.() -> Unit),
) {
    CompositionLocalProvider(
        LocalContentColor provides prezelFloatingMenuButtonContentColor(style.hierarchy),
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
        ) {
            PrezelFloatingButtonMenu(
                isExpanded = isExpanded,
                style = style,
                content = content,
            )

            PrezelMainFloatingButton(
                iconSource = iconSource,
                style = style,
                isExpanded = isExpanded,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun PrezelMainFloatingButton(
    iconSource: IconSource,
    style: PrezelFloatingButtonStyle,
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentIconSource =
        if (isExpanded) {
            DrawableIcon(
                resId = PrezelIcons.Cancel,
                contentDescTextId = R.string.core_designsystem_close_floating_btn_content_desc,
            )
        } else {
            iconSource
        }

    PrezelFloatingButton(
        iconSource = currentIconSource,
        onClick = onClick,
        modifier = modifier,
        style = style,
    )
}

@Composable
private fun PrezelFloatingButtonMenu(
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    style: PrezelFloatingButtonStyle,
    content: @Composable ColumnScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Column(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .background(
                    shape = PrezelTheme.shapes.V12,
                    color = prezelFloatingMenuButtonContainerColor(style.hierarchy),
                ).padding(prezelFloatingMenuButtonPaddingValues(style.size)),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V4),
        ) {
            CompositionLocalProvider(
                LocalPrezelFloatingButtonMenuItemSize provides PrezelFloatingButtonMenuItemSize.buttonMenuItemSize(style.size),
            ) {
                content()
            }
        }
    }
}

@ThemePreview
@Composable
private fun PrimaryPrezelFloatingMenuButtonPreview() {
    PrezelTheme {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .requiredHeightIn(200.dp)
                .background(PrezelTheme.colors.bgScrim)
                .padding(12.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PreviewFloatingMenuButton(
                initialExpanded = false,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.PRIMARY, size = PrezelFloatingButtonSize.REGULAR),
            )
            PreviewFloatingMenuButton(
                initialExpanded = true,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.PRIMARY, size = PrezelFloatingButtonSize.REGULAR),
            )
            PreviewFloatingMenuButton(
                initialExpanded = false,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.PRIMARY, size = PrezelFloatingButtonSize.SMALL),
            )
            PreviewFloatingMenuButton(
                initialExpanded = true,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.PRIMARY, size = PrezelFloatingButtonSize.SMALL),
            )
        }
    }
}

@ThemePreview
@Composable
private fun SecondaryPrezelFloatingMenuButtonPreview() {
    PrezelTheme {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .requiredHeightIn(200.dp)
                .background(PrezelTheme.colors.bgScrim)
                .padding(12.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PreviewFloatingMenuButton(
                initialExpanded = false,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.SECONDARY, size = PrezelFloatingButtonSize.REGULAR),
            )
            PreviewFloatingMenuButton(
                initialExpanded = true,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.SECONDARY, size = PrezelFloatingButtonSize.REGULAR),
            )
            PreviewFloatingMenuButton(
                initialExpanded = false,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.SECONDARY, size = PrezelFloatingButtonSize.SMALL),
            )
            PreviewFloatingMenuButton(
                initialExpanded = true,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.SECONDARY, size = PrezelFloatingButtonSize.SMALL),
            )
        }
    }
}

@Composable
private fun PreviewFloatingMenuButton(
    initialExpanded: Boolean,
    style: PrezelFloatingButtonStyle,
) {
    var isExpanded by remember { mutableStateOf(initialExpanded) }

    PrezelFloatingMenuButton(
        iconSource = DrawableIcon(resId = PrezelIcons.Blank),
        isExpanded = isExpanded,
        onClick = { isExpanded = !isExpanded },
        style = style,
    ) {
        PrezelFloatingButtonMenuItem(label = "LongLabel", onClick = {})
        PrezelFloatingButtonMenuItem(label = "Label", onClick = {})
        PrezelFloatingButtonMenuItem(label = "Label", onClick = {})
    }
}

@ThemePreview
@Composable
private fun PrezelFloatingMenuButtonPreview() {
    PrezelTheme {
        Row {
            PreviewFloatingMenuButton(true)
            PreviewFloatingMenuButton(false)
        }
    }
}

@Composable
private fun PreviewFloatingMenuButton(isShowIcon: Boolean) {
    val iconSource = if (isShowIcon) DrawableIcon(resId = PrezelIcons.Blank) else null

    PrezelFloatingMenuButton(
        modifier = Modifier
            .background(PrezelTheme.colors.bgScrim)
            .padding(16.dp),
        iconSource = DrawableIcon(resId = PrezelIcons.Blank),
        isExpanded = true,
        onClick = {},
    ) {
        PrezelFloatingButtonMenuItem(label = "LongLabel", onClick = {}, iconSource = iconSource)
        PrezelFloatingButtonMenuItem(label = "Label", onClick = {}, iconSource = iconSource)
        PrezelFloatingButtonMenuItem(label = "Label", onClick = {}, iconSource = iconSource)
    }
}
