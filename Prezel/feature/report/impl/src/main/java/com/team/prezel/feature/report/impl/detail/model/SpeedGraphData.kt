package com.team.prezel.feature.report.impl.detail.model

internal data class SpeedGraphData(
    val spm: Int,
    val result: SpeedResult,
)

internal enum class SpeedResult {
    FAST,
    ADEQUATE,
    SLOW,
}
