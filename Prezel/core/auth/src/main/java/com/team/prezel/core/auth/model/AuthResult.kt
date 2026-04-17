package com.team.prezel.core.auth.model

sealed interface AuthResult {
    data class Success(
        val idToken: String,
    ) : AuthResult

    data object Cancelled : AuthResult

    sealed interface Failure : AuthResult {
        data object Unknown : Failure

        data object RateLimited : Failure
    }
}
