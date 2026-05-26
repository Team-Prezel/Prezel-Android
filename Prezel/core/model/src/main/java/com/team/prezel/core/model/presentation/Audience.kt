package com.team.prezel.core.model.presentation

enum class Audience(
    val value: String,
) {
    GENERAL("GENERAL"),
    PROFESSIONAL("PROFESSIONAL"),
    TEAMMATE("TEAMMATE"),
    ;

    companion object {
        fun from(value: String): Audience =
            entries.find { entry ->
                entry.value.equals(other = value, ignoreCase = true)
            } ?: throw IllegalArgumentException("지원하지 않는 청중 타입입니다.")
    }
}
