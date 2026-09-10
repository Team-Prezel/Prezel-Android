package com.team.prezel.core.model.presentation

enum class PresentationAnalysisStatus(
    val value: String,
) {
    IN_PROCESS("inProcess"),
    COMPLETE("complete"),
    DEFAULT("default"),
    FAIL("fail"),
    ;

    companion object {
        fun from(value: String): PresentationAnalysisStatus =
            entries.find { entry ->
                entry.value.equals(other = value, ignoreCase = true)
            } ?: DEFAULT
    }
}
