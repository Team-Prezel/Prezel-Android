package com.team.prezel.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Stable
class IconSource private constructor(
    private val painterProvider: @Composable () -> Painter,
    private val contentDescProvider: @Composable () -> String?,
) {
    @Composable
    fun painter(): Painter = painterProvider()

    @Composable
    fun contentDescription(): String? = contentDescProvider()

    constructor(painter: Painter, contentDescription: String? = null) : this(
        painterProvider = { painter },
        contentDescProvider = { contentDescription },
    )

    constructor(
        @DrawableRes resId: Int,
        @StringRes contentDescResId: Int? = null,
        vararg args: String,
    ) : this(
        painterProvider = { painterResource(resId) },
        contentDescProvider = { contentDescResId?.let { stringResource(it, *args) } },
    )
}
