package com.team.prezel.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.team.prezel.core.navigation.toEntries
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.home.impl.HomeScreen

@Composable
fun PrezelApp(appState: PrezelAppState) {
    val navigationState = appState.navigationState
    val navigator = appState.navigator

    Scaffold { paddingValues ->
        val entryMapper: (NavKey) -> NavEntry<NavKey> = { key ->
            NavEntry(key) {
                when (key) {
                    HomeNavKey -> HomeScreen(modifier = Modifier.padding(paddingValues))
                    // HistoryNavKey -> HistoryRoute()
                    // ProfileNavKey -> ProfileRoute()
                    else -> Unit
                }
            }
        }

        NavDisplay(
            entries = navigationState.toEntries(entryMapper),
            onBack = { navigator.goBack() },
        )
    }
}
