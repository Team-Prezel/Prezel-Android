package com.team.prezel.core.ui.state

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope

val LocalSnackbarCoroutineScope = staticCompositionLocalOf<CoroutineScope> {
    error("SnackbarCoroutineScope is not provided")
}
