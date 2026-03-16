package com.team.prezel.core.designsystem.component.snackbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelColorScheme
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
) {
    val visuals = data.visuals as? PrezelSnackbarVisuals ?: error("PrezelSnackbar를 사용해주세요.")

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(PrezelTheme.spacing.V20)
            .offset(y = visuals.offsetY),
        shape = PrezelTheme.shapes.V12,
        color = PrezelColorScheme.Dark.bgMedium,
        contentColor = PrezelColorScheme.Dark.textLarge,
    ) {
        Row(
            modifier = Modifier.padding(start = PrezelTheme.spacing.V16, end = PrezelTheme.spacing.V8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            visuals.leadingIcon?.let { leadingIcon ->
                PrezelSnackbarLeadingIcon(icon = leadingIcon)
                Spacer(Modifier.width(PrezelTheme.spacing.V8))
            }

            Text(
                text = visuals.message,
                style = PrezelTextStyles.Body3Regular.toTextStyle(),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = PrezelTheme.spacing.V14),
            )

            visuals.actionLabel?.let { actionLabel ->
                Spacer(Modifier.width(PrezelTheme.spacing.V16))
                PrezelButton(
                    text = actionLabel,
                    onClick = { data.performAction() },
                    style = PrezelButtonStyle(
                        buttonType = PrezelButtonType.GHOST,
                        buttonSize = PrezelButtonSize.SMALL,
                    ),
                )
            }
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
private fun PrezelSnackBarPreview() {
    PrezelTheme {
        Column {
            PrezelSnackbar(
                data = previewData(message = "Message", actionLabel = "Action", leadingIcon = IconSource(PrezelIcons.Blank)),
            )

            PrezelSnackbar(
                data = previewData(message = "Message Message Message Message Message", actionLabel = "Action"),
            )
        }
    }
}

@ThemePreview
@Composable
private fun PrezelToastPreview() {
    PrezelTheme {
        Column {
            PrezelSnackbar(
                data = previewData(message = "Message", leadingIcon = IconSource(PrezelIcons.Blank)),
            )

            PrezelSnackbar(
                data = previewData(message = "Message Message Message Message Message"),
            )
        }
    }
}

@Composable
private fun previewData(
    message: String,
    actionLabel: String? = null,
    leadingIcon: IconSource? = null,
): SnackbarData =
    PreviewSnackbarData(
        visuals = PrezelSnackbarVisuals(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = false,
            duration = SnackbarDuration.Short,
            leadingIcon = leadingIcon,
            offsetY = 0.dp,
        ),
    )

private class PreviewSnackbarData(
    override val visuals: SnackbarVisuals,
) : SnackbarData {
    override fun dismiss() = Unit

    override fun performAction() = Unit
}
