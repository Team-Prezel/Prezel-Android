package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.icon.DrawableIcon
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelColorScheme
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Immutable
data class PrezelSnackBarStyle(
    val iconSize: Dp = 20.dp,
    val contentPadding: PaddingValues = PaddingValues(
        start = 16.dp,
        end = 8.dp,
        top = 6.dp,
        bottom = 6.dp,
    ),
    val shape: Shape = RoundedCornerShape(12.dp),
    val iconMessageSpacing: Dp = 8.dp,
    val messageActionSpacing: Dp = 16.dp,
    val actionTouchPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
)

@Immutable
data class PrezelSnackBarAction(
    val label: String,
    val onClick: () -> Unit,
)

@Composable
fun PrezelSnackBar(
    message: String,
    modifier: Modifier = Modifier,
    leadingIcon: IconSource? = null,
    action: PrezelSnackBarAction? = null,
    style: PrezelSnackBarStyle = PrezelSnackBarStyle(),
) {
    Surface(
        modifier = modifier,
        shape = style.shape,
        color = PrezelColorScheme.Dark.bgMedium,
    ) {
        PrezelSnackBarContent(
            message = message,
            leadingIcon = leadingIcon,
            action = action,
            style = style,
        )
    }
}

@Composable
private fun PrezelSnackBarContent(
    message: String,
    modifier: Modifier = Modifier,
    leadingIcon: IconSource? = null,
    action: PrezelSnackBarAction?,
    style: PrezelSnackBarStyle = PrezelSnackBarStyle(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(style.contentPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            PrezelSnackBarLeadingIcon(icon = leadingIcon, style = style)
            Spacer(Modifier.width(style.iconMessageSpacing))
        }

        Text(
            text = message,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .weight(1f),
            color = PrezelColorScheme.Dark.textLarge,
            overflow = TextOverflow.Ellipsis,
            style = PrezelTextStyles.Body3Regular.toTextStyle(),
        )

        action?.let {
            Spacer(Modifier.width(style.messageActionSpacing))

            Text(
                text = it.label,
                modifier = Modifier
                    .clickable(role = Role.Button, onClick = it.onClick)
                    .padding(style.actionTouchPadding),
                color = PrezelColorScheme.Dark.interactiveRegular,
                maxLines = 1,
                style = PrezelTextStyles.Body3Medium.toTextStyle(),
            )
        }
    }
}

@Composable
private fun PrezelSnackBarLeadingIcon(
    icon: IconSource,
    style: PrezelSnackBarStyle,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = icon.painter(),
        contentDescription = icon.contentDescription(),
        modifier = modifier.size(style.iconSize),
        tint = PrezelColorScheme.Dark.iconLarge,
    )
}

@ThemePreview
@Composable
private fun PrezelSnackBarPreview() {
    PrezelTheme {
        Column(
            modifier = Modifier
                .background(PrezelTheme.colors.bgRegular)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val previewAction = PrezelSnackBarAction(
                label = "Action",
                onClick = {},
            )

            Text("SnackBar")
            SnackBarPreviewItem(
                message = "Message",
                icon = DrawableIcon(PrezelIcons.Blank),
                action = previewAction,
            )
            SnackBarPreviewItem(
                message = "Message Message Message Message Message",
                icon = DrawableIcon(PrezelIcons.Blank),
                action = previewAction,
            )
            SnackBarPreviewItem(
                message = "Message",
                icon = null,
                action = previewAction,
            )
            SnackBarPreviewItem(
                message = "Message Message Message Message Message",
                icon = null,
                action = previewAction,
            )

            Text("Toast")
            SnackBarPreviewItem(message = "Message", icon = null)
            SnackBarPreviewItem(message = "Message Message Message Message Message Message", icon = null)
            SnackBarPreviewItem(message = "Message", icon = DrawableIcon(PrezelIcons.Blank))
            SnackBarPreviewItem(message = "Message Message Message Message Message Message", icon = DrawableIcon(PrezelIcons.Blank))
        }
    }
}

@Composable
private fun SnackBarPreviewItem(
    message: String,
    icon: IconSource?,
    action: PrezelSnackBarAction? = null,
) {
    PrezelSnackBar(
        message = message,
        leadingIcon = icon,
        action = action,
        modifier = Modifier.fillMaxWidth(),
    )
}
