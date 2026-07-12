package com.team.prezel.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import timber.log.Timber

@Composable
fun PrezelAsyncImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    onSuccess: () -> Unit = {},
    onError: (Throwable?) -> Unit = {},
) {
    val context = LocalContext.current
    val imageLoader = remember(context) {
        ImageLoader
            .Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }.build()
    }

    AsyncImage(
        model = url,
        imageLoader = imageLoader,
        contentDescription = contentDescription,
        modifier = modifier,
        placeholder = ColorPainter(Color.Transparent),
        onSuccess = { onSuccess() },
        onError = { error ->
            onError(error.result.throwable)
            Timber.e(t = error.result.throwable)
        },
        contentScale = contentScale,
    )
}
