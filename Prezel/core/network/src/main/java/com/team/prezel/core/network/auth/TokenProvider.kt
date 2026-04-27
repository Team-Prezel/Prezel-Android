package com.team.prezel.core.network.auth

import com.team.prezel.core.model.auth.AuthTokens

interface TokenProvider {
    suspend fun getTokens(): AuthTokens?
    suspend fun updateTokens(
        accessToken: String,
        refreshToken: String,
    )
    suspend fun clearTokens()
}
