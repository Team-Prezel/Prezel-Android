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
    onLoading: () -> Unit = {},
    onSuccess: () -> Unit = {},
    onFailure: (Throwable?) -> Unit = {},
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        placeholder = ColorPainter(Color.Transparent),
        onLoading = { onLoading() },
        onSuccess = { onSuccess() },
        onError = { onFailure(it.result.throwable) },
        contentScale = contentScale,
    )
}
