package com.team.prezel.feature.badge.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class BadgeNavKey(
    val badgeCode: String,
) : NavKey
