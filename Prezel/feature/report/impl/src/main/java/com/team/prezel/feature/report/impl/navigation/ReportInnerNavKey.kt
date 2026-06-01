package com.team.prezel.feature.report.impl.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface ReportInnerNavKey : NavKey {
    @Serializable
    data class ScriptCorrection(
        val analysisResultId: Long,
    ) : ReportInnerNavKey
}
