package com.team.prezel.core.network.model

sealed interface ApiResponse<out T> {
    data class Success<T>(
        val data: T,
    ) : ApiResponse<T>

    data class Error(
        val code: Int,
        val message: String,
    ) : ApiResponse<Nothing>

    data object NetworkError : ApiResponse<Nothing>
}
