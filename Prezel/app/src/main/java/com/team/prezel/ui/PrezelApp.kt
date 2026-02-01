package com.team.prezel.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.team.prezel.core.designsystem.component.PrezelNavigationScaffold
import com.team.prezel.core.navigation.toEntries
import com.team.prezel.feature.home.impl.navigation.homeEntry
import com.team.prezel.navigation.TOP_LEVEL_KEYS
import com.team.prezel.navigation.TOP_LEVEL_NAV_ITEMS
import com.team.prezel.navigation.currentTopLevelKeyOrStart

@Composable
fun PrezelApp(appState: PrezelAppState) {
    val navigationState = appState.navigationState
    val navigator = appState.navigator
    val currentTopLevelKey = navigationState.currentTopLevelKeyOrStart()
    val entryProvider = entryProvider {
        homeEntry(navigator)
        // historyEntry(navigator)
        // profileEntry(navigator)
    }

    PrezelNavigationScaffold(
        showNavigationBar = navigationState.currentKey in TOP_LEVEL_KEYS,
        navigationItems = {
            TOP_LEVEL_NAV_ITEMS.forEach { (key, item) ->
                item(
                    selected = key == currentTopLevelKey,
                    onClick = { navigator.navigate(key) },
                    labelTextId = item.titleTextId,
                    iconRes = item.iconRes,
                )
            }
        },
    ) { padding ->
        NavDisplay(
            entries = navigationState.toEntries(entryProvider = entryProvider),
            onBack = { navigator.goBack() },
            modifier = Modifier.padding(padding),
        )
    }
}
