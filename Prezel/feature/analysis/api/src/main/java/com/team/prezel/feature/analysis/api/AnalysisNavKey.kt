package com.team.prezel.feature.analysis.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AnalysisNavKey : NavKey {
    @Serializable
    data object Create : AnalysisNavKey
}
