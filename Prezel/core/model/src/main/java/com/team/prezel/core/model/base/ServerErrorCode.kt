package com.team.prezel.core.model.base

enum class ServerErrorCode(
    val code: String,
) {
    INVALID_REQUEST("C001"),
    SERVER_ERROR("C002"),

    UNAUTHORIZED("U001"),
    FORBIDDEN("U002"),
    USER_NOT_FOUND("U003"),
    DUPLICATE_NICKNAME("U004"),

    INVALID_TOKEN("T001"),
    TOKEN_STOLEN("T002"),
    INVALID_ID_TOKEN("T003"),

    TERMS_NOT_FOUND("TR001"),
    REQUIRED_TERMS_DISAGREED("TR002"),

    FILE_IS_EMPTY("F001"),
    FILE_UPLOAD_FAILED("F002"),

    UNKNOWN("UNKNOWN"),
    ;

    companion object {
        fun from(code: String?): ServerErrorCode {
            return entries.firstOrNull { it.code == code } ?: UNKNOWN
        }
    }
}
