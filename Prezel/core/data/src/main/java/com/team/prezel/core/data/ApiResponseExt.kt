package com.team.prezel.core.data

import com.team.prezel.core.network.model.ApiResponse

internal inline fun <T, R> ApiResponse<T>.toResult(transform: (T) -> R): Result<R> =
    when (this) {
        is ApiResponse.Success -> Result.success(transform(data))
        is ApiResponse.Failure.HttpError -> Result.failure(throwable)
        is ApiResponse.Failure.NetworkError -> Result.failure(throwable)
    }
