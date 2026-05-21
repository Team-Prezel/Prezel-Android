package com.team.prezel.feature.report.impl.detail.model

internal data class ScriptAnalysisGraphData(
    val spellingCount: Int,
    val grammarCount: Int,
) {
    val totalErrorCount: Int = spellingCount + grammarCount
}
