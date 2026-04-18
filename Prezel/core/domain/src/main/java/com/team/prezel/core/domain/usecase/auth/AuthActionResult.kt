package com.team.prezel.core.domain.usecase.auth

sealed interface AuthActionResult {
    data object Success : AuthActionResult

    data object AuthenticationRequired : AuthActionResult

    data class Failure(
        val throwable: Throwable,
    ) : AuthActionResult
}
