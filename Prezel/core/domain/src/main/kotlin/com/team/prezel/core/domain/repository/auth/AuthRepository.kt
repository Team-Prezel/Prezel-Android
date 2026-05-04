package com.team.prezel.core.domain.repository.auth

import com.team.prezel.core.model.auth.AuthCheckResult
import com.team.prezel.core.model.auth.WithdrawReason
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authCheckResult: Flow<AuthCheckResult>

    suspend fun logout(): Result<Unit>

    suspend fun login(idToken: String): Result<Unit>

    suspend fun withdraw(reason: WithdrawReason): Result<Unit>
}
