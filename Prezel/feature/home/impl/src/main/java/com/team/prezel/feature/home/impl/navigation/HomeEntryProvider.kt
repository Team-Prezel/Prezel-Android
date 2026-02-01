package com.team.prezel.feature.home.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.Navigator
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.home.impl.HomeScreen

fun EntryProviderScope<NavKey>.homeEntry(navigator: Navigator) {
    entry<HomeNavKey> {
        HomeScreen()
    }
}
