package com.team.prezel.core.auth

import android.content.Context
import com.team.prezel.core.auth.model.AuthResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    private val authClient: AuthClient,
) {
    private var isLoggedInToOAuthProvider: Boolean = false

    suspend fun login(context: Context): AuthResult {
        val result = authClient.login(context = context)

        if (result is AuthResult.Success) {
            isLoggedInToOAuthProvider = true
        }

        return result
    }

    suspend fun logout(): Result<Unit> {
        if (!isLoggedInToOAuthProvider) {
            return Result.failure(IllegalStateException("로그인된 OAuth 세션이 없습니다."))
        }

        return authClient.logout().onSuccess {
            isLoggedInToOAuthProvider = false
        }
    }

    fun clearLoginState() {
        isLoggedInToOAuthProvider = false
    }

    suspend fun clearAuthSession(): Result<Unit> {
        if (!isLoggedInToOAuthProvider) {
            return Result.success(Unit)
        }

        return authClient
            .logout()
            .also { isLoggedInToOAuthProvider = false }
    }
}
