package com.team.prezel.core.designsystem.component.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.icon.IconSource

data class PrezelSnackbarVisuals(
    override val message: String,
    override val actionLabel: String?,
    override val withDismissAction: Boolean,
    override val duration: SnackbarDuration,
    val leadingIcon: IconSource?,
) : SnackbarVisuals

suspend fun SnackbarHostState.showPrezelSnackbar(
    message: String,
    leadingIcon: IconSource? = null,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onAction: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
) {
    val result = showSnackbar(
        visuals = PrezelSnackbarVisuals(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            duration = duration,
            leadingIcon = leadingIcon,
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
        PrezelSnackbar(
            data = data,
            leadingIcon = data.visuals.leadingIconOrNull(),
        )
    }
}

internal fun SnackbarVisuals.leadingIconOrNull(): IconSource? = (this as? PrezelSnackbarVisuals)?.leadingIcon
