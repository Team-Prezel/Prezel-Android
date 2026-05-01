package com.team.prezel.core.domain.repository.auth

import com.team.prezel.core.model.auth.LoginStatus
import com.team.prezel.core.model.auth.WithdrawReason
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val loginStatus: Flow<LoginStatus>

    suspend fun logout(): Result<Unit>

    suspend fun login(idToken: String): Result<Unit>

    suspend fun withdraw(reason: WithdrawReason): Result<Unit>
}
