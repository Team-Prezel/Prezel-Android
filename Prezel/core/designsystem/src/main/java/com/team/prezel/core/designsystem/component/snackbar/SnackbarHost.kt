package com.team.prezel.core.designsystem.component.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
    leadingIcon: IconSource?,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = SnackbarDuration.Short,
) {
    showSnackbar(
        visuals = PrezelSnackbarVisuals(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            duration = duration,
            leadingIcon = leadingIcon,
        ),
    )
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
        val visuals = data.visuals
        val leadingIcon = (visuals as? PrezelSnackbarVisuals)?.leadingIcon

        PrezelSnackbar(
            data = data,
            leadingIcon = leadingIcon,
        )
    }
}
