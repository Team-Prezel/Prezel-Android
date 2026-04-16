package com.team.prezel.feature.profile.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface ProfileNavKey : NavKey {
    @Serializable
    data object Create : ProfileNavKey

    @Serializable
    data class Edit(
        val nickname: String,
        val profileUrl: String,
    ) : ProfileNavKey
}
