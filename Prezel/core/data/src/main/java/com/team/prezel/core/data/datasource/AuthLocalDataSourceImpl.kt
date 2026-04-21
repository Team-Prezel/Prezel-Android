package com.team.prezel.core.data.datasource

import com.team.prezel.core.datastore.auth.AuthTokenStore
import javax.inject.Inject

internal class AuthLocalDataSourceImpl @Inject constructor(
    private val authTokenStore: AuthTokenStore,
) : AuthLocalDataSource {
    override suspend fun awaitInitialized() {
        authTokenStore.awaitInitialized()
    }

    override fun getAccessToken(): String? = authTokenStore.getAccessToken()

    override fun getRefreshToken(): String? = authTokenStore.getRefreshToken()

    override suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        authTokenStore.saveTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    override suspend fun clear() {
        authTokenStore.clear()
    }
}
