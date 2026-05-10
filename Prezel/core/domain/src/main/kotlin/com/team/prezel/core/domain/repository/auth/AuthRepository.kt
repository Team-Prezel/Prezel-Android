package com.team.prezel.core.domain.repository.auth

import com.team.prezel.core.model.auth.WithdrawReason

interface AuthRepository {
    suspend fun hasJwtToken(): Result<Boolean>

    suspend fun logout(): Result<Unit>

    suspend fun login(idToken: String): Result<Unit>

    suspend fun clearSession(): Result<Unit>

    suspend fun withdraw(reason: WithdrawReason): Result<Unit>
}
