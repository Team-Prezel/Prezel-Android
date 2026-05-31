package com.team.prezel.feature.analysis.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
sealed interface AnalysisNavKey : NavKey {
    val flowId: String
    val startType: AnalysisStartType

    @Serializable
    data class Schedule(
        override val flowId: String = newAnalysisFlowId(),
        override val startType: AnalysisStartType = AnalysisStartType.VOICE_RECORDING,
    ) : AnalysisNavKey

    @Serializable
    data class Situation(
        override val flowId: String,
        override val startType: AnalysisStartType,
    ) : AnalysisNavKey

    @Serializable
    data class Script(
        override val flowId: String,
        override val startType: AnalysisStartType,
    ) : AnalysisNavKey

    @Serializable
    data class AudioUpload(
        override val flowId: String,
        override val startType: AnalysisStartType,
    ) : AnalysisNavKey

    @Serializable
    data class Recording(
        override val flowId: String,
        override val startType: AnalysisStartType,
    ) : AnalysisNavKey

    @Serializable
    data class Analyzing(
        override val flowId: String,
        override val startType: AnalysisStartType,
    ) : AnalysisNavKey

    @Serializable
    data class ReRecording(
        val presentationId: Long,
        val isPast: Boolean = false,
        override val flowId: String = newAnalysisFlowId(),
        override val startType: AnalysisStartType = AnalysisStartType.VOICE_RECORDING,
    ) : AnalysisNavKey

    @Serializable
    data class ReWritingScript(
        val presentationId: Long,
        val isPast: Boolean = false,
        override val flowId: String = newAnalysisFlowId(),
        override val startType: AnalysisStartType = AnalysisStartType.VOICE_RECORDING,
    ) : AnalysisNavKey
}

@Serializable
enum class AnalysisStartType {
    VOICE_RECORDING,
    FILE_UPLOAD,
}

private fun newAnalysisFlowId(): String = UUID.randomUUID().toString()
