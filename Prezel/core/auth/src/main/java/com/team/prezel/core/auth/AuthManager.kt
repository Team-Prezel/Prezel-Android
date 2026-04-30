package com.team.prezel.core.auth

import android.content.Context
import com.team.prezel.core.auth.model.AuthResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    private val authClient: AuthClient,
) {
    suspend fun login(context: Context): AuthResult = authClient.login(context = context)

    suspend fun logout(): Result<Unit> {
        if (!authClient.isLoggedIn()) return Result.success(Unit)
        return authClient.logout()
    }

    suspend fun unlink(): Result<Unit> {
        if (!authClient.isLoggedIn()) return Result.success(Unit)
        return authClient.unlink()
    }
}
