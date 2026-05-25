package com.team.prezel.core.model.presentation

enum class Purpose(
    val value: String,
) {
    INFO("INFO"),
    UNDERSTANDING("UNDERSTANDING"),
    EMPATHY("EMPATHY"),
    ;

    companion object {
        fun from(value: String): Purpose =
            entries.find { entry ->
                entry.value == value.uppercase()
            } ?: throw IllegalArgumentException("지원하지 않는 발표 목적입니다.")
    }
}
