package com.team.prezel.core.designsystem.component.snackbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.icon.DrawableIcon
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelColorScheme
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Immutable
internal object PrezelSnackbarDefaults {
    val ContentPadding = PaddingValues(
        start = 16.dp,
        end = 8.dp,
        top = 6.dp,
        bottom = 6.dp,
    )

    val ActionTouchPadding = PaddingValues(
        horizontal = 12.dp,
        vertical = 8.dp,
    )
}

@Composable
fun PrezelSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
    leadingIcon: IconSource? = null,
) {
    Surface(
        modifier = modifier,
        shape = PrezelTheme.shapes.V12,
        color = PrezelColorScheme.Dark.bgMedium,
    ) {
        PrezelSnackbarContent(
            data = data,
            leadingIcon = leadingIcon,
        )
    }
}

@Composable
fun PrezelSnackbarContent(
    data: SnackbarData,
    modifier: Modifier = Modifier,
    leadingIcon: IconSource? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .padding(PrezelSnackbarDefaults.ContentPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingIcon?.let {
            PrezelSnackbarLeadingIcon(icon = it)
            Spacer(Modifier.width(PrezelTheme.spacing.V8))
        }

        Text(
            text = data.visuals.message,
            modifier = Modifier
                .padding(top = PrezelTheme.spacing.V8, bottom = PrezelTheme.spacing.V8, end = PrezelTheme.spacing.V8)
                .weight(1f),
            color = PrezelColorScheme.Dark.textLarge,
            style = PrezelTextStyles.Body3Regular.toTextStyle(),
        )

        data.visuals.actionLabel?.let { label ->
            Spacer(Modifier.width(PrezelTheme.spacing.V16))

            Text(
                text = label,
                modifier = Modifier
                    .clickable(role = Role.Button, onClick = { data.performAction() })
                    .padding(PrezelSnackbarDefaults.ActionTouchPadding),
                color = PrezelColorScheme.Dark.interactiveRegular,
                style = PrezelTextStyles.Body3Medium.toTextStyle(),
            )
        }
    }
}

@Composable
private fun PrezelSnackbarLeadingIcon(
    icon: IconSource,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = icon.painter(),
        contentDescription = icon.contentDescription(),
        modifier = modifier.size(20.dp),
        tint = PrezelColorScheme.Dark.iconLarge,
    )
}

@ThemePreview
@Composable
private fun PrezelSnackBarPreview_Cases() {
    PrezelTheme {
        PreviewScaffold {
            Text("Action O / Icon O")
            PrezelSnackbar(
                data = previewData(message = "Message", actionLabel = "Action"),
                leadingIcon = DrawableIcon(PrezelIcons.Blank),
                modifier = Modifier.fillMaxWidth(),
            )

            Text("Action O / Icon X")
            PrezelSnackbar(
                data = previewData(message = "Message Message Message Message Message", actionLabel = "Action"),
                leadingIcon = null,
                modifier = Modifier.fillMaxWidth(),
            )

            Text("Action X / Icon O")
            PrezelSnackbar(
                data = previewData(message = "Message", actionLabel = null),
                leadingIcon = DrawableIcon(PrezelIcons.Blank),
                modifier = Modifier.fillMaxWidth(),
            )

            Text("Action X / Icon X")
            PrezelSnackbar(
                data = previewData(message = "Message Message Message Message Message", actionLabel = null),
                leadingIcon = null,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun previewData(
    message: String,
    actionLabel: String?,
): SnackbarData =
    PreviewSnackbarData(
        visuals = PreviewSnackbarVisuals(
            message = message,
            actionLabel = actionLabel,
        ),
    )

private data class PreviewSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
) : SnackbarVisuals

@Suppress("EmptyFunctionBlock")
private class PreviewSnackbarData(
    override val visuals: SnackbarVisuals,
) : SnackbarData {
    override fun dismiss() {}

    override fun performAction() {}
}
