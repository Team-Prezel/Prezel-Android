package com.team.prezel.core.domain.repository.auth

import com.team.prezel.core.domain.result.auth.AuthActionResult
import com.team.prezel.core.domain.result.auth.LoginStatusResult
import com.team.prezel.core.model.auth.WithdrawReason

interface AuthRepository {
    suspend fun checkLoginStatus(): LoginStatusResult

    suspend fun logout(): AuthActionResult

    suspend fun login(idToken: String): Result<Unit>

    suspend fun withdraw(reason: WithdrawReason): AuthActionResult
}
