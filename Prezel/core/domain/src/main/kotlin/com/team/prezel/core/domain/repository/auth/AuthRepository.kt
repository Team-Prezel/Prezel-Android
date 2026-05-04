package com.team.prezel.core.domain.repository.auth

import com.team.prezel.core.model.auth.WithdrawReason
import com.team.prezel.core.model.profile.User

interface AuthRepository {
    suspend fun checkLoginStatus(): Result<User?>

    suspend fun logout(): Result<Unit>

    suspend fun login(idToken: String): Result<User?>

    suspend fun withdraw(reason: WithdrawReason): Result<Unit>
}
