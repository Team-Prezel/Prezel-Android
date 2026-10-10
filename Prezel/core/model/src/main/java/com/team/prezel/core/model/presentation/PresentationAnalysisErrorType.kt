package com.team.prezel.core.model.presentation

enum class PresentationAnalysisErrorType(
    val value: String,
) {
    VOICE_RECOGNITION("ERR_VOICE_RECOG"),
    FILE_RECOGNITION("ERR_FILE_RECOG"),
    ANALYSIS("ERR_ANALYSIS"),
    ;

    companion object {
        fun from(value: String?): PresentationAnalysisErrorType? =
            value?.let { rawValue ->
                entries.find { entry ->
                    entry.value.equals(other = rawValue, ignoreCase = true)
                } ?: ANALYSIS
            }
    }
}
