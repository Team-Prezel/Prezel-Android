package com.team.prezel.core.designsystem.component.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal data class PrezelSnackbarVisuals(
    override val message: String,
    override val actionLabel: String?,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration,
    val id: String? = null,
    @param:DrawableRes val leadingIconResId: Int?,
    val offsetY: Dp,
) : SnackbarVisuals

suspend fun SnackbarHostState.showPrezelSnackbar(
    message: String,
    actionLabel: String,
    onAction: () -> Unit,
    @DrawableRes leadingIconResId: Int? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    id: String? = null,
    onDismiss: (() -> Unit)? = null,
    offsetY: Dp = 0.dp,
) {
    val result = showSnackbar(
        visuals = PrezelSnackbarVisuals(
            message = message,
            actionLabel = actionLabel,
            duration = duration,
            id = id,
            leadingIconResId = leadingIconResId,
            offsetY = offsetY,
        ),
    )

    when (result) {
        SnackbarResult.ActionPerformed -> onAction?.invoke()
        SnackbarResult.Dismissed -> onDismiss?.invoke()
    }
}

@Composable
fun PrezelSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
    ) { data ->
        PrezelSnackbar(data = data)
    }
}

fun SnackbarHostState.dismissById(id: String) {
    val visuals = currentSnackbarData?.visuals as? PrezelSnackbarVisuals ?: return
    if (visuals.id == id) currentSnackbarData?.dismiss()
}
