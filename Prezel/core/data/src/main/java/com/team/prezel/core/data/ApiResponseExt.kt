package com.team.prezel.core.data

import com.team.prezel.core.domain.error.ApiHttpException
import com.team.prezel.core.network.model.ApiResponse

internal suspend inline fun <T, R> ApiResponse<T>.toResult(crossinline transform: suspend (T) -> R): Result<R> =
    when (this) {
        is ApiResponse.Success -> Result.success(transform(data))
        is ApiResponse.Failure.HttpError ->
            Result.failure(
                ApiHttpException(
                    status = error?.status,
                    code = error?.code,
                    message = error?.message,
                    cause = throwable,
                ),
            )
        is ApiResponse.Failure.NetworkError -> Result.failure(throwable)
    }
