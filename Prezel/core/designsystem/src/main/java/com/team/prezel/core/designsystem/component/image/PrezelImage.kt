package com.team.prezel.core.designsystem.component.image

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
import androidx.compose.runtime.LaunchedEffect
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
    source: PrezelImageSource,
    contentDescription: String,
    modifier: Modifier = Modifier,
    rounded: Boolean = false,
    border: Boolean = false,
    onSuccess: () -> Unit = {},
    onError: (Throwable?) -> Unit = {},
) {
    val shape = if (rounded) RoundedCornerShape(12.dp) else RectangleShape
    val borderStroke = if (border) BorderStroke(width = 1.dp, color = PrezelTheme.colors.borderRegular) else null

    Box(
        modifier = modifier
            .size(100.dp)
            .clip(shape)
            .then(if (borderStroke != null) Modifier.border(borderStroke, shape) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        when (source) {
            is PrezelImageSource.Url -> {
                PrezelAsyncImage(
                    url = source.value,
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onSuccess = onSuccess,
                    onError = onError,
                )
            }

            is PrezelImageSource.Drawable -> {
                Image(
                    painter = painterResource(id = source.resId),
                    contentDescription = contentDescription,
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Fit,
                )

                LaunchedEffect(Unit) {
                    onSuccess()
                }
            }
        }
    }
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
            Text(text = "Drawable / No Round / No Border")
            PrezelImage(
                source = PrezelImageSource.Drawable(resId = PrezelIcons.Calendar),
                contentDescription = "Drawable / No Round / No Border",
                rounded = false,
                border = false,
            )

            Text(text = "Drawable / No Round / No Border")
            PrezelImage(
                source = PrezelImageSource.Drawable(resId = PrezelIcons.Calendar),
                contentDescription = "Drawable / Round / No Border",
                rounded = true,
                border = false,
            )

            Text(text = "Drawable / Round / Border")
            PrezelImage(
                source = PrezelImageSource.Drawable(resId = PrezelIcons.Calendar),
                contentDescription = "Drawable / Round / Border",
                rounded = true,
                border = true,
            )
        }
    }
}
