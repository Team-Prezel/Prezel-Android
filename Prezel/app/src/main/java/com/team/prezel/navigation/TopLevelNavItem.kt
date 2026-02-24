package com.team.prezel.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.team.prezel.R
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.feature.history.api.HistoryNavKey
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.profile.api.ProfileNavKey
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentMapOf

data class TopLevelNavItem(
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val titleTextId: Int,
)

val HOME = TopLevelNavItem(
    iconRes = PrezelIcons.Home,
    titleTextId = R.string.bottom_nav_home,
)

val HISTORY = TopLevelNavItem(
    iconRes = PrezelIcons.Storage,
    titleTextId = R.string.bottom_nav_history,
)

val PROFILE = TopLevelNavItem(
    iconRes = PrezelIcons.Profile,
    titleTextId = R.string.bottom_nav_profile,
)

val TOP_LEVEL_NAV_ITEMS = persistentMapOf(
    HomeNavKey to HOME,
    HistoryNavKey to HISTORY,
    ProfileNavKey to PROFILE,
)

val TOP_LEVEL_KEYS: ImmutableSet<NavKey> = TOP_LEVEL_NAV_ITEMS.keys
