package com.team.prezel.core.designsystem.component.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.preview.PreviewValueRow
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

@BasicPreview
@Composable
private fun PrezelImagePreview() {
    PreviewSection(
        title = "Image",
        description = "round/border 조합별 이미지 콘텐츠를 보여줍니다.",
    ) {
        PreviewValueRow(name = "No Round / No Border") {
            PrezelImage(
                resId = PrezelIcons.Blank,
                contentDescription = "",
                modifier = Modifier.size(100.dp),
                rounded = false,
                border = false,
            )
        }

        PreviewValueRow(name = "Round / No Border") {
            PrezelImage(
                resId = PrezelIcons.Blank,
                contentDescription = "",
                modifier = Modifier.size(100.dp),
                rounded = true,
                border = false,
            )
        }

        PreviewValueRow(name = "No Round / Border") {
            PrezelImage(
                resId = PrezelIcons.Blank,
                contentDescription = "",
                modifier = Modifier.size(100.dp),
                rounded = false,
                border = true,
            )
        }

        PreviewValueRow(name = "Round / Border") {
            PrezelImage(
                resId = PrezelIcons.Blank,
                contentDescription = "",
                modifier = Modifier.size(100.dp),
                rounded = true,
                border = true,
            )
        }
    }
}
