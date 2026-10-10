package com.team.prezel.feature.history.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object HistoryNavKey : NavKey

@Serializable
data class HistoryFailedAnalysisNavKey(
    val presentationId: Long,
    val analysisResultId: Long?,
    val errorType: String?,
    val script: String?,
    val audioUrl: String?,
) : NavKey
