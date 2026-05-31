package com.team.prezel.ui

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.team.prezel.R
import com.team.prezel.core.common.event.GlobalEvent
import com.team.prezel.core.common.event.GlobalEventBus
import com.team.prezel.core.designsystem.component.PrezelNavigationScaffold
import com.team.prezel.core.designsystem.component.PrezelNavigationScope
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.domain.usecase.badge.ConnectBadgeEventStreamUseCase
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.core.navigation.Navigator
import com.team.prezel.core.navigation.ProvideSharedTransitionScope
import com.team.prezel.core.navigation.toEntries
import com.team.prezel.core.ui.state.LocalAppDimmerState
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.core.ui.state.rememberAppDimmerState
import com.team.prezel.core.ui.util.noRippleClickable
import com.team.prezel.feature.badge.api.BadgeNavKey
import com.team.prezel.feature.splash.api.SplashNavKey
import com.team.prezel.navigation.MAIN_NAV_ITEMS
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun PrezelApp(
    appState: PrezelAppState,
    globalEventBus: GlobalEventBus,
    connectBadgeEventStreamUseCase: ConnectBadgeEventStreamUseCase,
    entryBuilders: ImmutableSet<EntryProviderScope<NavKey>.() -> Unit>,
) {
    val navigator = remember(appState.navigationState) { Navigator(appState.navigationState) }
    val snackbarHostState = remember { SnackbarHostState() }
    val appDimmerState = rememberAppDimmerState()

    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalAppDimmerState provides appDimmerState,
        LocalSnackbarHostState provides snackbarHostState,
    ) {
        DoubleBackToExitHandler(navigationState = appState.navigationState)

        PrezelAppContent(
            appState = appState,
            globalEventBus = globalEventBus,
            connectBadgeEventStreamUseCase = connectBadgeEventStreamUseCase,
            entryBuilders = entryBuilders,
        )
    }
}

@Composable
private fun PrezelAppContent(
    appState: PrezelAppState,
    globalEventBus: GlobalEventBus,
    connectBadgeEventStreamUseCase: ConnectBadgeEventStreamUseCase,
    entryBuilders: ImmutableSet<EntryProviderScope<NavKey>.() -> Unit>,
) {
    val navigator = LocalNavigator.current
    val appDimmerState = LocalAppDimmerState.current

    ObserveGlobalEvents(
        globalEventBus = globalEventBus,
        navigateToSplash = { navigator.replaceRoot(SplashNavKey) },
    )

    ObserveBadgeEvents(
        connectBadgeEventStreamUseCase = connectBadgeEventStreamUseCase,
        navigateToBadge = { navigator.navigate(BadgeNavKey) },
    )

    Box(modifier = Modifier.fillMaxSize()) {
        SharedTransitionLayout {
            ProvideSharedTransitionScope(this@SharedTransitionLayout) {
                AppNavigationContent(
                    appState = appState,
                    entryBuilders = entryBuilders,
                    navigator = navigator,
                )
            }
        }

        AppDimmerOverlay(isVisible = appDimmerState.isVisible, onDismiss = appDimmerState::dismiss)
    }
}

@Composable
private fun AppNavigationContent(
    appState: PrezelAppState,
    entryBuilders: ImmutableSet<EntryProviderScope<NavKey>.() -> Unit>,
    navigator: Navigator,
) {
    val provider = remember(entryBuilders, navigator) {
        entryProvider {
            entryBuilders.forEach { builder -> this.builder() }
        }
    }

    PrezelNavigationScaffold(
        showNavigationBar = appState.shouldShowNavigationBar,
        snackbarHostState = LocalSnackbarHostState.current,
        navigationItems = { AppNavigationItems(appState = appState, navigateToKey = navigator::navigate) },
    ) { padding ->
        NavDisplay(
            entries = appState.navigationState.toEntries(provider),
            onBack = navigator::goBack,
            modifier = Modifier.padding(padding),
            transitionSpec = { defaultPrezelNavTransition() },
            popTransitionSpec = { defaultPrezelNavTransition() },
            predictivePopTransitionSpec = { _: Int -> defaultPrezelNavTransition() },
        )
    }
}

@Composable
private fun AppDimmerOverlay(
    isVisible: Boolean,
    onDismiss: () -> Unit,
) {
    if (!isVisible) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.scrimContainer)
            .noRippleClickable(onClick = onDismiss),
    )
}

private fun defaultPrezelNavTransition(): ContentTransform =
    fadeIn(animationSpec = tween(durationMillis = 100)) togetherWith
        fadeOut(animationSpec = tween(durationMillis = 100))

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
private fun ObserveBadgeEvents(
    connectBadgeEventStreamUseCase: ConnectBadgeEventStreamUseCase,
    navigateToBadge: () -> Unit,
) {
    val snackbarHostState = LocalSnackbarHostState.current
    val context = LocalContext.current

    LaunchedEffect(connectBadgeEventStreamUseCase) {
        connectBadgeEventStreamUseCase().collect { event ->
            val message =
                event.message
                    ?.takeIf(String::isNotBlank)
                    ?: event.badgeName
                        ?.takeIf(String::isNotBlank)
                        ?.let { badgeName ->
                            context.getString(R.string.app_badge_event_message_with_name, badgeName)
                        }
                    ?: context.getString(R.string.app_badge_event_message)

            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showPrezelSnackbar(
                message = message,
                actionLabel = context.getString(R.string.app_badge_event_action),
                onAction = navigateToBadge,
            )
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
