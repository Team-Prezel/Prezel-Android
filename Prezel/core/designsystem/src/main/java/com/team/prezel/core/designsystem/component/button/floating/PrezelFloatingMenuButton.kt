package com.team.prezel.core.designsystem.component.button.floating

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        LocalContentColor provides PrezelTheme.colors.textMedium,
        LocalTextStyle provides PrezelTheme.typography.body2Regular,
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
        ) {
            PrezelFloatingButtonMenu(
                isExpanded = isExpanded,
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
    val currentIconSource = if (isExpanded) DrawableIcon(resId = PrezelIcons.Cancel, contentDescription = "플로팅 버튼 닫기") else iconSource

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
    content: @Composable ColumnScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Column(
            modifier = modifier
                .wrapContentSize()
                .background(
                    shape = PrezelTheme.shapes.V12,
                    color = PrezelTheme.colors.solidWhite,
                ).padding(PrezelTheme.spacing.V4),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V4),
            content = content,
        )
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
                .background(PrezelTheme.colors.bgRegular)
                .padding(12.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PreviewFloatingMenuButton(
                isExpanded = false,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.PRIMARY, size = PrezelFloatingButtonSize.REGULAR),
            )
            PreviewFloatingMenuButton(
                isExpanded = true,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.PRIMARY, size = PrezelFloatingButtonSize.REGULAR),
            )
            PreviewFloatingMenuButton(
                isExpanded = false,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.PRIMARY, size = PrezelFloatingButtonSize.SMALL),
            )
            PreviewFloatingMenuButton(
                isExpanded = true,
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
                .background(PrezelTheme.colors.bgRegular)
                .padding(12.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PreviewFloatingMenuButton(
                isExpanded = false,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.SECONDARY, size = PrezelFloatingButtonSize.REGULAR),
            )
            PreviewFloatingMenuButton(
                isExpanded = true,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.SECONDARY, size = PrezelFloatingButtonSize.REGULAR),
            )
            PreviewFloatingMenuButton(
                isExpanded = false,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.SECONDARY, size = PrezelFloatingButtonSize.SMALL),
            )
            PreviewFloatingMenuButton(
                isExpanded = true,
                style = PrezelFloatingButtonStyle(hierarchy = PrezelFloatingButtonHierarchy.SECONDARY, size = PrezelFloatingButtonSize.SMALL),
            )
        }
    }
}

@Composable
private fun PreviewFloatingMenuButton(
    isExpanded: Boolean,
    style: PrezelFloatingButtonStyle,
) {
    PrezelFloatingMenuButton(
        iconSource = DrawableIcon(resId = PrezelIcons.Blank),
        isExpanded = isExpanded,
        onClick = {},
        style = style,
    ) {
        Text(text = "Label")
        Text(text = "Label")
        Text(text = "Label")
    }
}
