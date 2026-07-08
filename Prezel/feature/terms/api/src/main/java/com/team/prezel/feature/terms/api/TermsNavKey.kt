package com.team.prezel.feature.terms.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface TermsNavKey : NavKey {
    @Serializable
    data object List : TermsNavKey

    @Serializable
    data class Detail(
        val title: String,
        val url: String,
    ) : TermsNavKey
}
