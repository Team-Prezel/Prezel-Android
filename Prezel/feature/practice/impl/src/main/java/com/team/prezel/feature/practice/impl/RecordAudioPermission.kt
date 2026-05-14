package com.team.prezel.feature.practice.impl

import android.Manifest
import androidx.compose.runtime.Composable
import com.team.prezel.core.audio.AudioSessionState
import com.team.prezel.core.ui.util.rememberPermissionRequest

@Composable
internal fun rememberRecordAudioPermissionControlClickHandler(
    recordingState: AudioSessionState,
    onClickRecordingControl: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionPermanentlyDenied: () -> Unit,
): () -> Unit {
    val permissionRequest = rememberPermissionRequest(
        permission = Manifest.permission.RECORD_AUDIO,
        onPermissionGranted = onClickRecordingControl,
        onPermissionDenied = onPermissionDenied,
        onPermissionPermanentlyDenied = onPermissionPermanentlyDenied,
    )

    return {
        when (recordingState) {
            AudioSessionState.Idle -> {
                when {
                    permissionRequest.isGranted -> onClickRecordingControl()
                    permissionRequest.isPermanentlyDenied -> permissionRequest.onPermanentlyDenied()
                    else -> permissionRequest.launch()
                }
            }

            is AudioSessionState.Recording,
            is AudioSessionState.ReadyToPlay,
            is AudioSessionState.Playing,
            -> onClickRecordingControl()
        }
    }
}
