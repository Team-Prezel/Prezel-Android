package com.team.prezel.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun PrezelAsyncImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    onSuccess: () -> Unit = {},
    onError: (Throwable?) -> Unit = {},
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        placeholder = ColorPainter(Color.Transparent),
        onSuccess = { onSuccess() },
        onError = { onError(it.result.throwable) },
        contentScale = contentScale,
    )
}
