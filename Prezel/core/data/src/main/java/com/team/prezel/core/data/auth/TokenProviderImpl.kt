package com.team.prezel.core.data.auth

import com.team.prezel.core.datastore.auth.AuthLocalDataSource
import com.team.prezel.core.network.auth.TokenProvider
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class TokenProviderImpl @Inject constructor(
    private val dataSource: AuthLocalDataSource,
) : TokenProvider {
    override suspend fun getTokens(): TokenProvider.AuthTokens? = dataSource.tokens.firstOrNull()

    override suspend fun updateTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        dataSource.saveTokens(accessToken = accessToken, refreshToken = refreshToken)
    }

    override suspend fun clearTokens() {
        dataSource.clearTokens()
    }
}
