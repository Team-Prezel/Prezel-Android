package com.team.prezel.feature.report.impl.navigation

import androidx.navigation3.runtime.NavKey
import com.team.prezel.feature.report.impl.accuracydetail.AccuracyDetailTab
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface ReportInnerNavKey : NavKey {
    val analysisResultId: Long

    @Serializable
    data class AccuracyDetail(
        override val analysisResultId: Long,
        val initialTab: AccuracyDetailTab,
    data class ScriptCorrection(
        val analysisResultId: Long,
    ) : ReportInnerNavKey
}
