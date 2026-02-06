package com.team.prezel.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.feature.home.api.HomeNavKey
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentMapOf
import com.team.prezel.feature.home.api.R as homeR

data class TopLevelNavItem(
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val titleTextId: Int,
)

val HOME = TopLevelNavItem(
    iconRes = PrezelIcons.Home,
    titleTextId = homeR.string.feature_home_api_title,
)

val HISTORY = TopLevelNavItem(
    iconRes = PrezelIcons.Home,
    titleTextId = homeR.string.feature_home_api_title,
)

val PROFILE = TopLevelNavItem(
    iconRes = PrezelIcons.Profile,
    titleTextId = homeR.string.feature_home_api_title,
)

val TOP_LEVEL_NAV_ITEMS = persistentMapOf(
    HomeNavKey to HOME,
//    HistoryNavKey to HISTORY,
//    ProfileNavKey to PROFILE,
)

val TOP_LEVEL_KEYS: ImmutableSet<NavKey> = TOP_LEVEL_NAV_ITEMS.keys
