package com.team.prezel.core.network.model

sealed interface ApiResponse<out T> {
    data class Success<T>(
        val data: T,
    ) : ApiResponse<T>

    sealed interface Failure : ApiResponse<Nothing> {
        data class HttpError(
            val throwable: Throwable,
        ) : Failure

        data object NetworkError : Failure
    }
}
