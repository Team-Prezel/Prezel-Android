package com.team.prezel.core.model.presentation

data class PresentationScriptDetail(
    val originalScript: String,
    val scriptCorrections: List<ScriptCorrection>,
)

data class ScriptCorrection(
    val errorType: ScriptErrorType,
    val sentence: String,
    val originalText: String,
    val correctedText: String,
    val reason: String,
    val startIndex: Int,
    val endIndex: Int,
)

enum class ScriptErrorType(
    val value: String,
) {
    GRAMMAR("GRAMMAR"),
    SPELLING("SPELLING"),
    ;

    companion object {
        fun from(value: String): ScriptErrorType =
            entries.find { entry ->
                entry.value == value
            } ?: throw IllegalArgumentException("지원하지 않는 에러 타입입니다.")
    }
}
