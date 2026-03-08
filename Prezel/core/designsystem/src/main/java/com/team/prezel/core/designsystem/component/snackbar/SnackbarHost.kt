package com.team.prezel.core.designsystem.component.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.IconSource

internal data class PrezelSnackbarVisuals(
    override val message: String,
    override val actionLabel: String,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration,
    val leadingIcon: IconSource?,
    val offsetY: Dp,
) : SnackbarVisuals

suspend fun SnackbarHostState.showPrezelSnackbar(
    message: String,
    actionLabel: String,
    onAction: () -> Unit,
    leadingIcon: IconSource? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onDismiss: (() -> Unit)? = null,
    offsetY: Dp = 0.dp,
) {
    val result = showSnackbar(
        visuals = PrezelSnackbarVisuals(
            message = message,
            actionLabel = actionLabel,
            duration = duration,
            leadingIcon = leadingIcon,
            offsetY = offsetY,
        ),
    )

    when (result) {
        SnackbarResult.ActionPerformed -> onAction.invoke()
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
