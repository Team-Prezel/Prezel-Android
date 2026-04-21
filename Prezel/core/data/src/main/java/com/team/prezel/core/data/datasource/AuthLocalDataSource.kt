package com.team.prezel.core.data.datasource

internal interface AuthLocalDataSource {
    suspend fun awaitInitialized()

    fun getAccessToken(): String?

    fun getRefreshToken(): String?

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    )

    suspend fun clear()
}
