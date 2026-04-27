package com.team.prezel.core.auth.model

sealed interface AuthResult {
    data class Success(
        val idToken: String,
    ) : AuthResult

    data object Cancelled : AuthResult

    data class Failure(
        val throwable: Throwable,
    ) : AuthResult
}
