package com.team.prezel.core.model.presentation

enum class Style(
    val value: String,
) {
    FORMAL("FORMAL"),
    FRIENDLY("FRIENDLY"),
    CALM("CALM"),
    CASUAL("CASUAL"),
    ;

    companion object {
        fun from(value: String): Style =
            entries.find { entry ->
                entry.value.equals(other = value, ignoreCase = true)
            } ?: throw IllegalArgumentException("지원하지 않는 발표 스타일입니다.")
    }
}
