package com.team.prezel.core.ui.state

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

@Stable
class AppDimmerState internal constructor() {
    var isVisible by mutableStateOf(false)
        private set

    var foregroundContent by mutableStateOf<(@Composable BoxScope.() -> Unit)?>(null)
        private set

    private var onDismissRequest: (() -> Unit)? by mutableStateOf(null)

    fun show(
        onDismissRequest: () -> Unit = {},
        foregroundContent: (@Composable BoxScope.() -> Unit)? = null,
    ) {
        this.onDismissRequest = onDismissRequest
        this.foregroundContent = foregroundContent
        isVisible = true
    }

    fun hide() {
        isVisible = false
        onDismissRequest = null
        foregroundContent = null
    }

    fun dismiss() {
        onDismissRequest?.invoke()
    }
}

@Composable
fun rememberAppDimmerState(): AppDimmerState = remember { AppDimmerState() }

val LocalAppDimmerState = staticCompositionLocalOf<AppDimmerState> {
    error("AppDimmerState is not provided")
}
