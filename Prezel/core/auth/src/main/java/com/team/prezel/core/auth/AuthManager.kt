package com.team.prezel.core.auth

import android.content.Context
import com.team.prezel.core.auth.model.AuthProvider
import com.team.prezel.core.auth.model.AuthResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager
    @Inject
    constructor(
        private val authClients: Map<AuthProvider, @JvmSuppressWildcards AuthClient>,
    ) {
        var currentProvider: AuthProvider? = null
            private set

        suspend fun login(
            context: Context,
            provider: AuthProvider,
        ): AuthResult {
            val authClient = authClients[provider] ?: return AuthResult.Failure.Unknown
            val result = authClient.login(context = context)

            if (result is AuthResult.Success) {
                currentProvider = provider
            }

            return result
        }

        suspend fun logout(): Result<Unit> {
            val provider = currentProvider ?: return Result.failure(
                IllegalStateException("로그인된 AuthProvider가 없습니다."),
            )

            val authClient = authClients[provider] ?: return Result.failure(
                IllegalStateException("해당 AuthProvider에 대한 AuthClient를 찾을 수 없습니다. provider=$provider"),
            )

            return authClient.logout().onSuccess {
                currentProvider = null
            }
        }
    }
