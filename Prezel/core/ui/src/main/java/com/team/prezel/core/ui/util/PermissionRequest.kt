package com.team.prezel.core.ui.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun rememberPermissionRequest(
    permission: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionPermanentlyDenied: () -> Unit,
): PermissionRequest {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnPermissionGranted by rememberUpdatedState(onPermissionGranted)
    val currentOnPermissionDenied by rememberUpdatedState(onPermissionDenied)
    val currentOnPermissionPermanentlyDenied by rememberUpdatedState(onPermissionPermanentlyDenied)
    var isGranted by remember(permission) { mutableStateOf(context.isPermissionGranted(permission)) }
    var hasRequestedPermission by remember(permission) { mutableStateOf(false) }
    var isPermanentlyDenied by remember(permission) { mutableStateOf(false) }

    fun syncPermissionState() {
        val syncedIsGranted = context.isPermissionGranted(permission)
        isGranted = syncedIsGranted
        isPermanentlyDenied = !syncedIsGranted &&
            hasRequestedPermission &&
            activity.isPermissionPermanentlyDenied(permission)
    }

    DisposableEffect(lifecycleOwner, context, activity, permission) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                syncPermissionState()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { launcherIsGranted ->
        hasRequestedPermission = true
        isGranted = launcherIsGranted
        isPermanentlyDenied = !launcherIsGranted && activity.isPermissionPermanentlyDenied(permission)
        when {
            launcherIsGranted -> {
                currentOnPermissionGranted()
            }
            isPermanentlyDenied -> {
                currentOnPermissionPermanentlyDenied()
            }
            else -> {
                currentOnPermissionDenied()
            }
        }
    }

    return PermissionRequest(
        isGranted = isGranted,
        isPermanentlyDenied = isPermanentlyDenied,
        launch = { launcher.launch(permission) },
        onPermanentlyDenied = currentOnPermissionPermanentlyDenied,
    )
}

private fun Context.isPermissionGranted(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

private fun Activity?.isPermissionPermanentlyDenied(permission: String): Boolean =
    this?.let {
        !ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
    } == true

data class PermissionRequest(
    val isGranted: Boolean,
    val isPermanentlyDenied: Boolean,
    val launch: () -> Unit,
    val onPermanentlyDenied: () -> Unit,
)
