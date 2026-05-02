package com.team.prezel.feature.home.impl.practice

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.team.prezel.feature.home.impl.practice.contract.PracticeRecordingState

@Composable
internal fun rememberRecordAudioPermissionControlClickHandler(
    recordingState: PracticeRecordingState,
    onClickControl: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionPermanentlyDenied: () -> Unit,
): () -> Unit {
    val permissionRequest = rememberRecordAudioPermissionRequest(
        onPermissionGranted = onClickControl,
        onPermissionDenied = onPermissionDenied,
        onPermissionPermanentlyDenied = onPermissionPermanentlyDenied,
    )
    val currentOnClickControl by rememberUpdatedState(onClickControl)

    return remember(recordingState, permissionRequest) {
        {
            when (recordingState) {
                PracticeRecordingState.Idle -> {
                    when {
                        permissionRequest.isGranted -> currentOnClickControl()
                        permissionRequest.isPermanentlyDenied -> permissionRequest.onPermanentlyDenied()
                        else -> permissionRequest.launch()
                    }
                }

                is PracticeRecordingState.Recording,
                is PracticeRecordingState.Recorded,
                is PracticeRecordingState.Playing,
                -> currentOnClickControl()
            }
        }
    }
}

@Composable
private fun rememberRecordAudioPermissionRequest(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionPermanentlyDenied: () -> Unit,
): RecordAudioPermissionRequest {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val currentOnPermissionGranted by rememberUpdatedState(onPermissionGranted)
    val currentOnPermissionDenied by rememberUpdatedState(onPermissionDenied)
    val currentOnPermissionPermanentlyDenied by rememberUpdatedState(onPermissionPermanentlyDenied)
    var hasRecordAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED,
        )
    }
    var isPermanentlyDenied by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        hasRecordAudioPermission = isGranted
        if (isGranted) {
            isPermanentlyDenied = false
            currentOnPermissionGranted()
        } else {
            isPermanentlyDenied = activity?.let {
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.RECORD_AUDIO,
                )
            } == true
            if (isPermanentlyDenied) {
                currentOnPermissionPermanentlyDenied()
            } else {
                currentOnPermissionDenied()
            }
        }
    }

    return remember(hasRecordAudioPermission, isPermanentlyDenied, launcher) {
        RecordAudioPermissionRequest(
            isGranted = hasRecordAudioPermission,
            isPermanentlyDenied = isPermanentlyDenied,
            launch = { launcher.launch(Manifest.permission.RECORD_AUDIO) },
            onPermanentlyDenied = currentOnPermissionPermanentlyDenied,
        )
    }
}

private data class RecordAudioPermissionRequest(
    val isGranted: Boolean,
    val isPermanentlyDenied: Boolean,
    val launch: () -> Unit,
    val onPermanentlyDenied: () -> Unit,
)
