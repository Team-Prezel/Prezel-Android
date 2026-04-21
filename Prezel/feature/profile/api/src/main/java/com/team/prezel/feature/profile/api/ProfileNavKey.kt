package com.team.prezel.feature.profile.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface ProfileNavKey : NavKey {
    @Serializable
    data object Create : ProfileNavKey

    @Serializable
    data object Edit : ProfileNavKey
}
