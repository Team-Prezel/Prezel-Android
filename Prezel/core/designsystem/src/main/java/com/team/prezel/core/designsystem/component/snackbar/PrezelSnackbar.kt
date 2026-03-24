package com.team.prezel.core.designsystem.component.snackbar

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
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
            visuals.leadingIconResId?.let { resId ->
                PrezelSnackbarLeadingIcon(iconResId = resId)
                Spacer(Modifier.width(PrezelTheme.spacing.V8))
            }

            Text(
                text = visuals.message,
                style = PrezelTextStyles.Body3Regular.toTextStyle(),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = PrezelTheme.spacing.V14),
            )

            Spacer(Modifier.width(PrezelTheme.spacing.V16))
            PrezelButton(
                text = visuals.actionLabel,
                type = ButtonType.GHOST,
                size = ButtonSize.SMALL,
                onClick = { data.performAction() },
            )
        }
    }
}

@Composable
private fun PrezelSnackbarLeadingIcon(
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = iconResId),
        contentDescription = null,
        modifier = modifier.size(20.dp),
        tint = PrezelColorScheme.Dark.iconLarge,
    )
}

@BasicPreview
@Composable
private fun PrezelSnackBarPreview_Cases() {
    PrezelTheme {
        Column {
            PrezelSnackbar(
                data = previewData(message = "Message", actionLabel = "Action", iconResId = PrezelIcons.Blank),
            )

            PrezelSnackbar(
                data = previewData(message = "Message Message Message Message Message", actionLabel = "Action"),
            )
        }
    }
}

@Composable
private fun previewData(
    message: String,
    actionLabel: String,
    @DrawableRes iconResId: Int? = null,
): SnackbarData =
    PreviewSnackbarData(
        visuals = PrezelSnackbarVisuals(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = false,
            duration = SnackbarDuration.Short,
            leadingIconResId = iconResId,
            offsetY = 0.dp,
        ),
    )

private class PreviewSnackbarData(
    override val visuals: SnackbarVisuals,
) : SnackbarData {
    override fun dismiss() = Unit

    override fun performAction() = Unit
}
