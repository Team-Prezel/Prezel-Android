package com.team.prezel.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Immutable
interface IconSource {
    @Composable
    fun painter(): Painter

    @Composable
    fun contentDescription(vararg args: String): String?
}

@Immutable
data class DrawableIcon(
    @param:DrawableRes val resId: Int,
    @param:StringRes val contentDescTextId: Int? = null,
) : IconSource {
    @Composable
    override fun painter(): Painter = painterResource(resId)

    @Composable
    override fun contentDescription(vararg args: String): String? = contentDescTextId?.let { resId -> stringResource(resId, *args) }
}
