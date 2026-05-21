package com.team.prezel.feature.report.api

import androidx.navigation3.runtime.NavKey
import com.team.prezel.feature.report.api.model.ReportAnalysisPayload
import kotlinx.serialization.Serializable

@Serializable
sealed interface ReportNavKey : NavKey {
    @Serializable
    data class History(
        val id: Long,
    ) : ReportNavKey

    @Serializable
    data class Analysis(
        val payload: ReportAnalysisPayload,
    ) : ReportNavKey
}
