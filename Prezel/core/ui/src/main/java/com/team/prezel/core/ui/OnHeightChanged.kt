package com.team.prezel.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Modifier.onHeightChanged(onHeightChanged: (Dp) -> Unit): Modifier {
    val density = LocalDensity.current

    return onSizeChanged { size ->
        with(density) { onHeightChanged(size.height.toDp()) }
    }
}
