package com.team.prezel.core.domain

import com.team.prezel.core.model.auth.AuthToken
import com.team.prezel.core.model.auth.WithdrawReason

interface AuthRepository {
    suspend fun reissueToken(refreshToken: String): Result<AuthToken>

    suspend fun logout(accessToken: String): Result<Unit>

    suspend fun login(idToken: String): Result<AuthToken>

    suspend fun withdraw(
        accessToken: String,
        reason: WithdrawReason,
    ): Result<Unit>
}
