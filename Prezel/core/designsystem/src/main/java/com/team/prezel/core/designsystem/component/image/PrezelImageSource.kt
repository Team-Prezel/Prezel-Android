package com.team.prezel.core.designsystem.component.image

import androidx.compose.runtime.Immutable

@Immutable
sealed interface PrezelImageSource {
    @Immutable
    data class Url(
        val value: String,
    ) : PrezelImageSource

    @Immutable
    data class Drawable(
        val resId: Int,
    ) : PrezelImageSource
}
