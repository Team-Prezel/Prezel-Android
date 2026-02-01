package com.team.prezel.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.team.prezel.core.data.NetworkMonitor
import com.team.prezel.core.navigation.NavigationState
import com.team.prezel.core.navigation.Navigator
import com.team.prezel.core.navigation.rememberNavigationState
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.navigation.TOP_LEVEL_NAV_ITEMS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberPrezelAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): PrezelAppState {
    val navigationState = rememberNavigationState(
        startKey = HomeNavKey,
        topLevelKeys = TOP_LEVEL_NAV_ITEMS.keys,
    )

    val navigator = remember(navigationState) { Navigator(navigationState) }

    return remember(
        navigationState,
        navigator,
        networkMonitor,
        coroutineScope,
    ) {
        PrezelAppState(
            navigationState = navigationState,
            navigator = navigator,
            networkMonitor = networkMonitor,
            coroutineScope = coroutineScope,
        )
    }
}

@Stable
class PrezelAppState(
    val navigationState: NavigationState,
    val navigator: Navigator,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope,
) {
    /**
     * true면 오프라인 상태
     */
    val isOffline: StateFlow<Boolean> =
        networkMonitor.isOnline
            .map(Boolean::not)
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false,
            )
}
