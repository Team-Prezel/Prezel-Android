package com.team.prezel.feature.report.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ReportNavKey(
    val presentationId: Long,
    val isPast: Boolean = false,
    val refreshKey: String = "",
) : NavKey
