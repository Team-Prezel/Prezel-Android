package com.team.prezel.core.domain


interface AuthRepository {
    suspend fun login(
        provider: String,
        idToken: String,
    ): Result<Unit>
}
