package com.team.prezel.feature.report.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface ReportNavKey : NavKey {
    @Serializable
    data class History(
        val id: Long,
    ) : ReportNavKey

    @Serializable
    data class Analysis(
        val presentationId: Long,
    ) : ReportNavKey
}
