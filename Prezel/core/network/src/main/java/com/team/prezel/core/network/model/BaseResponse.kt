package com.team.prezel.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("status")
    val status: Int,
    @SerialName("code")
    val code: String?,
    @SerialName("data")
    val data: T?,
    @SerialName("message")
    val message: String?,
)

internal fun <T> BaseResponse<T>.requireData(): T {
    requireSuccess()

    return data ?: throw ApiException(
        status = status,
        errorCode = ServerErrorCode.UNKNOWN,
        message = "Response data is null",
    )
}

internal fun BaseResponse<*>.requireSuccess() {
    if (status in 200..299) return

    throw ApiException(
        status = status,
        errorCode = ServerErrorCode.from(code),
        message = message.orEmpty(),
    )
}

class ApiException(
    val status: Int,
    val errorCode: ServerErrorCode,
    override val message: String,
) : Exception(message)

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
    SELF_FEEDBACK_ALREADY_WRITTEN("R002"),
    FILE_IS_EMPTY("F001"),
    FILE_UPLOAD_FAILED("F002"),
    UNSUPPORTED_FILE_FORMAT("F004"),
    PRESENTATION_REVIEW_NOT_FOUND("PR001"),
    PRESENTATION_REVIEW_FORBIDDEN("PR002"),
    PRESENTATION_NOT_FOUND("P001"),
    ANALYSIS_RESULT_NOT_FOUND("A001"),
    SENTENCE_NOT_FOUND("S001"),
    VOICE_RECOGNITION_FAILED("V001"),
    VOICE_ANALYSIS_FAILED("V002"),
    UNKNOWN("UNKNOWN"),
    ;

    companion object {
        fun from(code: String?): ServerErrorCode = entries.firstOrNull { it.code == code } ?: UNKNOWN
    }
}
