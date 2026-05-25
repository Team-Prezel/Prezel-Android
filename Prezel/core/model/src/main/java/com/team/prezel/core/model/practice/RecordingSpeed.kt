package com.team.prezel.core.model.practice

enum class RecordingSpeed(
    val value: String,
) {
    SLOW("느려요"),
    ADEQUATE("적당해요"),
    FAST("빨라요"),
    ;

    companion object {
        fun from(value: String): RecordingSpeed =
            entries.find { entry ->
                entry.value == value
            } ?: throw IllegalArgumentException("지원하지 않는 타입입니다.")
    }
}
