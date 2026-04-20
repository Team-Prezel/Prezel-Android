package com.team.prezel.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.team.prezel.R
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.feature.history.api.HistoryNavKey
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.login.api.LoginNavKey
import com.team.prezel.feature.my.api.MyNavKey
import com.team.prezel.feature.splash.api.SplashNavKey
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf

data class TopLevelNavItem(
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val titleTextId: Int,
)

private val HOME = TopLevelNavItem(
    iconRes = PrezelIcons.Home,
    titleTextId = R.string.bottom_nav_home,
)

private val HISTORY = TopLevelNavItem(
    iconRes = PrezelIcons.Storage,
    titleTextId = R.string.bottom_nav_history,
)

private val MY = TopLevelNavItem(
    iconRes = PrezelIcons.Profile,
    titleTextId = R.string.bottom_nav_profile,
)

internal val MAIN_NAV_ITEMS = persistentMapOf(
    HomeNavKey to HOME,
    HistoryNavKey to HISTORY,
    MyNavKey to MY,
)

internal val MAIN_NAV_KEYS: ImmutableSet<NavKey> = MAIN_NAV_ITEMS.keys

internal val TOP_LEVEL_KEYS: ImmutableSet<NavKey> = persistentSetOf(
    SplashNavKey,
    LoginNavKey,
).addAll(MAIN_NAV_KEYS)
