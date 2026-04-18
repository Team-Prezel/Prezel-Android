package com.team.prezel.core.domain.repository.auth

import com.team.prezel.core.domain.result.auth.AuthActionResult
import com.team.prezel.core.model.auth.AuthToken
import com.team.prezel.core.model.auth.WithdrawReason

interface AuthRepository {
    fun getAccessToken(): String?

    fun getRefreshToken(): String?

    suspend fun reissueToken(refreshToken: String): Result<AuthToken>

    suspend fun logout(): AuthActionResult

    suspend fun login(idToken: String): Result<AuthToken>

    suspend fun withdraw(reason: WithdrawReason): AuthActionResult
}
