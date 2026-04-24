package com.team.prezel.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalResources
import com.team.prezel.R
import com.team.prezel.core.designsystem.component.feedback.snackbar.dismissById
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.navigation.NavigationState
import com.team.prezel.core.ui.state.LocalSnackbarHostState

private const val SNACKBAR_IDENTIFIER = "DoubleBackToExitHandler"

@Stable
private sealed interface BackPressState {
    data object Idle : BackPressState

    data object Pressed : BackPressState
}

@Composable
internal fun DoubleBackToExitHandler(navigationState: NavigationState) {
    var backPressState by remember { mutableStateOf<BackPressState>(BackPressState.Idle) }
    val snackbarState = LocalSnackbarHostState.current
    val resources = LocalResources.current
    val isTopLevelScreen = navigationState.currentKey in navigationState.topLevelKeys

    LaunchedEffect(backPressState, isTopLevelScreen) {
        if (backPressState == BackPressState.Idle) {
            snackbarState.dismissById(SNACKBAR_IDENTIFIER)
            return@LaunchedEffect
        }

        if (!isTopLevelScreen) {
            backPressState = BackPressState.Idle
            return@LaunchedEffect
        }

        snackbarState.showPrezelSnackbar(
            id = SNACKBAR_IDENTIFIER,
            message = resources.getString(R.string.double_back_to_exit_snackbar_message),
            actionLabel = resources.getString(R.string.double_back_to_exit_snackbar_action_label),
            onAction = { backPressState = BackPressState.Idle },
            onDismiss = { backPressState = BackPressState.Idle },
        )
    }

    LaunchedEffect(navigationState.currentTopLevelKey) {
        backPressState = BackPressState.Idle
    }

    BackHandler(
        enabled = isTopLevelScreen && backPressState == BackPressState.Idle,
        onBack = {
            backPressState = BackPressState.Pressed
        },
    )
}
