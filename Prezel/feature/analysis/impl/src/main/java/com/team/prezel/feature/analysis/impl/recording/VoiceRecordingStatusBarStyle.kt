package com.team.prezel.feature.analysis.impl.recording

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.team.prezel.core.common.event.EdgeToEdgeStatusBarStyle
import com.team.prezel.core.common.event.GlobalEvent
import com.team.prezel.core.common.event.GlobalEventBus
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@Composable
internal fun VoiceRecordingStatusBarStyle(style: EdgeToEdgeStatusBarStyle) {
    if (LocalInspectionMode.current) return

    val globalEventBus = rememberGlobalEventBus()

    LaunchedEffect(globalEventBus, style) {
        globalEventBus.emit(GlobalEvent.ChangeEdgeToEdgeStatusBarStyle(style))
    }

    DisposableEffect(globalEventBus) {
        onDispose {
            globalEventBus.tryEmit(GlobalEvent.ResetEdgeToEdgeStatusBarStyle)
        }
    }
}

@Composable
private fun rememberGlobalEventBus(): GlobalEventBus {
    val applicationContext = LocalContext.current.applicationContext

    return remember(applicationContext) {
        EntryPointAccessors
            .fromApplication(
                applicationContext,
                VoiceRecordingGlobalEventBusEntryPoint::class.java,
            ).globalEventBus()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
private interface VoiceRecordingGlobalEventBusEntryPoint {
    fun globalEventBus(): GlobalEventBus
}
