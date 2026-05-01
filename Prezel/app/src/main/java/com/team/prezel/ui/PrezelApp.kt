package com.team.prezel.ui

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.team.prezel.core.common.event.GlobalEvent
import com.team.prezel.core.common.event.GlobalEventBus
import com.team.prezel.core.designsystem.component.PrezelNavigationScaffold
import com.team.prezel.core.designsystem.component.PrezelNavigationScope
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.core.navigation.Navigator
import com.team.prezel.core.navigation.ProvideSharedTransitionScope
import com.team.prezel.core.navigation.toEntries
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.splash.api.SplashNavKey
import com.team.prezel.navigation.MAIN_NAV_ITEMS
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun PrezelApp(
    appState: PrezelAppState,
    globalEventBus: GlobalEventBus,
    entryBuilders: ImmutableSet<EntryProviderScope<NavKey>.() -> Unit>,
) {
    val navigator = remember(appState.navigationState) { Navigator(appState.navigationState) }
    val snackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalSnackbarHostState provides snackbarHostState,
    ) {
        DoubleBackToExitHandler(navigationState = appState.navigationState)

        PrezelAppContent(
            appState = appState,
            globalEventBus = globalEventBus,
            entryBuilders = entryBuilders,
        )
    }
}

@Composable
private fun PrezelAppContent(
    appState: PrezelAppState,
    globalEventBus: GlobalEventBus,
    entryBuilders: ImmutableSet<EntryProviderScope<NavKey>.() -> Unit>,
) {
    val navigator = LocalNavigator.current

    ObserveGlobalEvents(
        globalEventBus = globalEventBus,
        navigateToSplash = { navigator.replaceRoot(SplashNavKey) },
    )

    SharedTransitionLayout {
        ProvideSharedTransitionScope(this@SharedTransitionLayout) {
            val provider = remember(entryBuilders, navigator) {
                entryProvider {
                    entryBuilders.forEach { builder -> this.builder() }
                }
            }

            PrezelNavigationScaffold(
                showNavigationBar = appState.shouldShowNavigationBar,
                snackbarHostState = LocalSnackbarHostState.current,
                navigationItems = { AppNavigationItems(appState = appState, navigateToKey = { key -> navigator.navigate(key) }) },
            ) { padding ->
                NavDisplay(
                    entries = appState.navigationState.toEntries(provider),
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
                    predictivePopTransitionSpec = {
                        fadeIn(animationSpec = tween(durationMillis = 100)) togetherWith
                            fadeOut(animationSpec = tween(durationMillis = 100))
                    },
                )
            }
        }
    }
}

@Composable
private fun ObserveGlobalEvents(
    globalEventBus: GlobalEventBus,
    navigateToSplash: () -> Unit,
) {
    LaunchedEffect(globalEventBus) {
        globalEventBus.events.collect { event ->
            when (event) {
                GlobalEvent.ForceLogout -> navigateToSplash()
            }
        }
    }
}

@Composable
private fun PrezelNavigationScope.AppNavigationItems(
    appState: PrezelAppState,
    navigateToKey: (NavKey) -> Unit,
) {
    MAIN_NAV_ITEMS.forEach { (key, item) ->
        Item(
            selected = key == appState.navigationState.currentTopLevelKey,
            onClick = { navigateToKey(key) },
            label = stringResource(item.titleTextId),
            iconResId = item.iconRes,
        )
    }
}
