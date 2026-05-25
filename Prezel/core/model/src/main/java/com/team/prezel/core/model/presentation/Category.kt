package com.team.prezel.core.model.presentation

enum class Category(
    val value: String,
) {
    EDUCATION("EDUCATION"),
    WORK("WORK"),
    OFFER("OFFER"),
    EVENT("EVENT"),
    ;

    companion object {
        fun from(value: String): Category =
            entries.find { entry ->
                entry.value == value.uppercase()
            } ?: throw IllegalArgumentException("지원하지 않는 발표 카테고리입니다.")
    }
}
