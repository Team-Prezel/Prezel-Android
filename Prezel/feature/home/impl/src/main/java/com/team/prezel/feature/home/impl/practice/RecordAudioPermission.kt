package com.team.prezel.feature.home.impl.practice

import android.Manifest
import androidx.compose.runtime.Composable
import com.team.prezel.core.ui.util.rememberPermissionRequest
import com.team.prezel.feature.home.impl.practice.model.PracticeRecordingState

@Composable
internal fun rememberRecordAudioPermissionControlClickHandler(
    recordingState: PracticeRecordingState,
    onClickControl: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionPermanentlyDenied: () -> Unit,
): () -> Unit {
    val permissionRequest = rememberPermissionRequest(
        permission = Manifest.permission.RECORD_AUDIO,
        onPermissionGranted = onClickControl,
        onPermissionDenied = onPermissionDenied,
        onPermissionPermanentlyDenied = onPermissionPermanentlyDenied,
    )

    return {
        when (recordingState) {
            PracticeRecordingState.Idle -> {
                when {
                    permissionRequest.isGranted -> onClickControl()
                    permissionRequest.isPermanentlyDenied -> permissionRequest.onPermanentlyDenied()
                    else -> permissionRequest.launch()
                }
            }

            is PracticeRecordingState.Recording,
            is PracticeRecordingState.Recorded,
            is PracticeRecordingState.Playing,
            -> onClickControl()
        }
    }
}
