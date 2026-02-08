package com.team.prezel.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.team.prezel.core.designsystem.component.PrezelNavigationScaffold
import com.team.prezel.core.navigation.Navigator
import com.team.prezel.core.navigation.toEntries
import com.team.prezel.navigation.TOP_LEVEL_NAV_ITEMS

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("Navigator is not provided")
}

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState is not provided")
}

@Composable
fun PrezelApp(
    appState: PrezelAppState,
    entryBuilders: Set<EntryProviderScope<NavKey>.() -> Unit>,
) {
    val navigator = remember(appState.navigationState) { Navigator(appState.navigationState) }
    val snackbarHostState = remember { SnackbarHostState() }
    val provider = entryProvider {
        entryBuilders.forEach { builder -> this.builder() }
    }


    PrezelAppRoot(
        appState = appState,
        navigator = navigator,
        snackbarHostState = snackbarHostState,
        entryProvider = provider,
    )
}

@Composable
private fun PrezelAppRoot(
    appState: PrezelAppState,
    navigator: Navigator,
    snackbarHostState: SnackbarHostState,
    entryProvider: (NavKey) -> NavEntry<NavKey>,
) {
    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalSnackbarHostState provides snackbarHostState,
    ) {
        PrezelNavigationScaffold(
            showNavigationBar = appState.shouldShowNavigationBar,
            snackbarHostState = snackbarHostState,
            navigationItems = {
                TOP_LEVEL_NAV_ITEMS.forEach { (key, item) ->
                    item(
                        selected = key == appState.navigationState.currentTopLevelKey,
                        onClick = { navigator.navigate(key) },
                        labelTextId = item.titleTextId,
                        iconRes = item.iconRes,
                    )
                }
            },
        ) { padding ->
            NavDisplay(
                entries = appState.navigationState.toEntries(entryProvider),
                onBack = navigator::goBack,
                modifier = Modifier.padding(padding),
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 100)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 100))
                },
                popTransitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 100)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 100))
                },
            )
        }
    }
}

