package com.team.prezel.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.team.prezel.core.data.NetworkMonitor
import com.team.prezel.core.domain.session.AuthSessionMonitor
import com.team.prezel.core.navigation.NavigationState
import com.team.prezel.core.navigation.rememberNavigationState
import com.team.prezel.feature.splash.api.SplashNavKey
import com.team.prezel.navigation.MAIN_NAV_KEYS
import com.team.prezel.navigation.TOP_LEVEL_KEYS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberPrezelAppState(
    networkMonitor: NetworkMonitor,
    authSessionMonitor: AuthSessionMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): PrezelAppState {
    val navigationState = rememberNavigationState(
        startKey = SplashNavKey,
        topLevelKeys = TOP_LEVEL_KEYS,
    )

    return remember(
        navigationState,
        coroutineScope,
        networkMonitor,
        authSessionMonitor,
    ) {
        PrezelAppState(
            navigationState = navigationState,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
            authSessionMonitor = authSessionMonitor,
        )
    }
}

@Stable
class PrezelAppState(
    val navigationState: NavigationState,
    val authSessionMonitor: AuthSessionMonitor,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
    val shouldShowNavigationBar
        get() = navigationState.currentKey in MAIN_NAV_KEYS

    val isOffline: StateFlow<Boolean> =
        networkMonitor.isOnline
            .map(Boolean::not)
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false,
            )
}
