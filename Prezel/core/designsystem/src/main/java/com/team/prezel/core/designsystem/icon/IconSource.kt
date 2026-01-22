package com.team.prezel.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

@Immutable
interface IconSource {
    val contentDescription: String?

    @Composable
    fun painter(): Painter
}

@Immutable
data class DrawableIcon(
    override val contentDescription: String? = null,
    @param:DrawableRes val resId: Int,
) : IconSource {
    @Composable
    override fun painter(): Painter = painterResource(resId)
}
