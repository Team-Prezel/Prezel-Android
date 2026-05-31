package com.team.prezel.core.data.error

import com.team.prezel.core.common.error.AppError
import com.team.prezel.core.common.error.AppException
import com.team.prezel.core.network.model.ApiException
import com.team.prezel.core.network.model.ServerErrorCode
import kotlinx.coroutines.CancellationException
import java.io.IOException

internal fun <T> Result<T>.mapDomainFailure(): Result<T> =
    fold(
        onSuccess = { Result.success(it) },
        onFailure = { throwable -> Result.failure(throwable.toDomainThrowable()) },
    )

private fun Throwable.toDomainThrowable(): Throwable =
    when (this) {
        is CancellationException -> this

        is ApiException ->
            AppException(
                error = errorCode.toDomainError(),
                message = message,
                cause = this,
            )

        is IOException ->
            AppException(
                error = AppError.NETWORK,
                message = message ?: "Network error",
                cause = this,
            )

        else ->
            AppException(
                error = AppError.UNKNOWN,
                message = message ?: "Unknown error",
                cause = this,
            )
    }

private fun ServerErrorCode.toDomainError(): AppError =
    when (this) {
        ServerErrorCode.INVALID_REQUEST,
        ServerErrorCode.REQUIRED_TERMS_DISAGREED,
        ServerErrorCode.FILE_UPLOAD_FAILED,
        ServerErrorCode.UNSUPPORTED_FILE_FORMAT,
        -> AppError.INVALID_REQUEST

        ServerErrorCode.FILE_IS_EMPTY -> AppError.SCRIPT_FILE_RECOGNITION_FAILED

        ServerErrorCode.SERVER_ERROR,
        ServerErrorCode.SENTENCE_NOT_FOUND,
        -> AppError.SERVER_ERROR

        ServerErrorCode.VOICE_ANALYSIS_FAILED -> AppError.VOICE_ANALYSIS_FAILED

        ServerErrorCode.TERMS_NOT_FOUND,
        ServerErrorCode.PRESENTATION_NOT_FOUND,
        ServerErrorCode.ANALYSIS_RESULT_NOT_FOUND,
        -> AppError.NOT_FOUND

        ServerErrorCode.DUPLICATE_NICKNAME -> AppError.DUPLICATE

        ServerErrorCode.UNAUTHORIZED,
        ServerErrorCode.FORBIDDEN,
        ServerErrorCode.INVALID_TOKEN,
        ServerErrorCode.TOKEN_STOLEN,
        ServerErrorCode.USER_NOT_FOUND,
        ServerErrorCode.INVALID_ID_TOKEN,
        -> AppError.UNAUTHORIZED

        ServerErrorCode.VOICE_RECOGNITION_FAILED -> AppError.VOICE_RECOGNITION_FAILED

        ServerErrorCode.UNKNOWN -> AppError.UNKNOWN
    }
