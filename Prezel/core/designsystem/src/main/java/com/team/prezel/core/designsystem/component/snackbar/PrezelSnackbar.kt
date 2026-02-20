package com.team.prezel.core.designsystem.component.snackbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.button.PrezelButton
import com.team.prezel.core.designsystem.component.button.PrezelButtonSize
import com.team.prezel.core.designsystem.component.button.PrezelButtonStyle
import com.team.prezel.core.designsystem.component.button.PrezelButtonType
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelColorScheme
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
) {
    val visuals = data.visuals
    val leadingIcon = visuals.leadingIconOrNull()
    val actionLabel = visuals.actionLabel

    Snackbar(
        modifier = modifier,
        shape = PrezelTheme.shapes.V12,
        containerColor = PrezelColorScheme.Dark.bgMedium,
        contentColor = PrezelColorScheme.Dark.textLarge,
        action = actionLabel?.let { label ->
            {
                PrezelButton(
                    text = label,
                    onClick = { data.performAction() },
                    style = PrezelButtonStyle(buttonType = PrezelButtonType.GHOST, buttonSize = PrezelButtonSize.SMALL),
                )
            }
        },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon?.let {
                PrezelSnackbarLeadingIcon(icon = it)
                Spacer(Modifier.width(PrezelTheme.spacing.V8))
            }

            Text(
                text = visuals.message,
                style = PrezelTextStyles.Body3Regular.toTextStyle(),
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

private fun SnackbarVisuals.leadingIconOrNull(): IconSource? = (this as? PrezelSnackbarVisuals)?.leadingIcon

@ThemePreview
@Composable
private fun PrezelSnackBarPreview_Cases() {
    PrezelTheme {
        PreviewScaffold {
            Text("Action O / Icon O")
            PrezelSnackbar(
                data = previewData(message = "Message", actionLabel = "Action", leadingIcon = IconSource(PrezelIcons.Blank)),
            )

            Text("Action X / Icon O")
            PrezelSnackbar(
                data = previewData(
                    message = "Message Message Message ",
                    actionLabel = null,
                    leadingIcon = IconSource(PrezelIcons.Blank),
                ),
            )

            Text("Action O / Icon X")
            PrezelSnackbar(
                data = previewData(message = "Message Message Message Message Message", actionLabel = "Action"),
            )

            Text("Action X / Icon X")
            PrezelSnackbar(
                data = previewData(message = "Message Message Message Message Message", actionLabel = null),
            )
        }
    }
}

@Composable
private fun previewData(
    message: String,
    actionLabel: String?,
    leadingIcon: IconSource? = null,
): SnackbarData =
    PreviewSnackbarData(
        visuals = PrezelSnackbarVisuals(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = false,
            duration = SnackbarDuration.Short,
            leadingIcon = leadingIcon,
        ),
    )

@Suppress("EmptyFunctionBlock")
private class PreviewSnackbarData(
    override val visuals: SnackbarVisuals,
) : SnackbarData {
    override fun dismiss() {}

    override fun performAction() {}
}
