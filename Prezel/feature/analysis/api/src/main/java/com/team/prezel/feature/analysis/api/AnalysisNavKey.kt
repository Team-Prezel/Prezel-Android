package com.team.prezel.feature.analysis.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
sealed interface AnalysisNavKey : NavKey {
    val flowId: String

    @Serializable
    data class Schedule(
        override val flowId: String = newAnalysisFlowId(),
    ) : AnalysisNavKey

    @Serializable
    data class Situation(
        override val flowId: String,
    ) : AnalysisNavKey

    @Serializable
    data class Script(
        override val flowId: String,
    ) : AnalysisNavKey

    @Serializable
    data class AudioUpload(
        override val flowId: String,
    ) : AnalysisNavKey

    @Serializable
    data class Recording(
        override val flowId: String,
    ) : AnalysisNavKey

    @Serializable
    data class Analyzing(
        override val flowId: String,
    ) : AnalysisNavKey

    @Serializable
    data class ReRecording(
        val presentationId: Long,
        val isPast: Boolean = false,
        override val flowId: String = newAnalysisFlowId(),
    ) : AnalysisNavKey

    @Serializable
    data class ReWritingScript(
        val presentationId: Long,
        val isPast: Boolean = false,
        override val flowId: String = newAnalysisFlowId(),
    ) : AnalysisNavKey
}

private fun newAnalysisFlowId(): String = UUID.randomUUID().toString()
