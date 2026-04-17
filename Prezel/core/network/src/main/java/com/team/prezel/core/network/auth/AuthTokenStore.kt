package com.team.prezel.core.network.auth

interface AuthTokenStore {
    fun getAccessToken(): String?

    fun getRefreshToken(): String?

    fun initializeCache()

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    )

    suspend fun clear()
}
