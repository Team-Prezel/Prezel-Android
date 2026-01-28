package com.team.prezel.core.designsystem.component.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelAsyncImage
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    rounded: Boolean = false,
    border: Boolean = false,
    onSuccess: () -> Unit = {},
    onError: (Throwable?) -> Unit = {},
) {
    Box(
        modifier = modifier.prezelImageContainer(rounded, border),
        contentAlignment = Alignment.Center,
    ) {
        PrezelAsyncImage(
            url = url,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            onSuccess = onSuccess,
            onError = onError,
        )
    }
}

@Composable
fun PrezelImage(
    @DrawableRes resId: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    rounded: Boolean = false,
    border: Boolean = false,
) {
    Box(
        modifier = modifier.prezelImageContainer(rounded, border),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = contentDescription,
            modifier = Modifier,
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
private fun Modifier.prezelImageContainer(
    rounded: Boolean,
    border: Boolean,
): Modifier {
    val shape = if (rounded) RoundedCornerShape(12.dp) else RectangleShape
    val borderStroke = if (border) BorderStroke(width = 1.dp, color = PrezelTheme.colors.borderRegular) else null

    return this
        .clip(shape)
        .then(if (borderStroke != null) Modifier.border(borderStroke, shape) else Modifier)
}

@ThemePreview
@Composable
private fun PrezelImagePreview() {
    PrezelTheme {
        Column(
            modifier = Modifier
                .background(PrezelTheme.colors.bgRegular)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PrezelImagePreviewItem(
                label = "Drawable / No Round / No Border",
                rounded = false,
                border = false,
            )

            PrezelImagePreviewItem(
                label = "Drawable / Round / No Border",
                rounded = true,
                border = false,
            )

            PrezelImagePreviewItem(
                label = "Drawable / No Round / Border",
                rounded = false,
                border = true,
            )

            PrezelImagePreviewItem(
                label = "Drawable / Round / Border",
                rounded = true,
                border = true,
            )
        }
    }
}

@Composable
private fun PrezelImagePreviewItem(
    label: String,
    rounded: Boolean,
    border: Boolean,
) {
    Text(text = label)
    PrezelImage(
        resId = PrezelIcons.Calendar,
        contentDescription = label,
        modifier = Modifier.size(100.dp),
        rounded = rounded,
        border = border,
    )
}
