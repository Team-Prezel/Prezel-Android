package com.team.prezel.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.team.prezel.core.data.NetworkMonitor
import com.team.prezel.core.navigation.NavigationState
import com.team.prezel.core.navigation.rememberNavigationState
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.navigation.TOP_LEVEL_KEYS
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

    return remember(
        navigationState,
        coroutineScope,
        networkMonitor,
    ) {
        PrezelAppState(
            navigationState = navigationState,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
        )
    }
}

@Stable
class PrezelAppState(
    val navigationState: NavigationState,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
    val shouldShowNavigationBar
        get() = navigationState.currentKey in TOP_LEVEL_KEYS

    val currentTopLevelKey
        get() = navigationState.currentKey

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
