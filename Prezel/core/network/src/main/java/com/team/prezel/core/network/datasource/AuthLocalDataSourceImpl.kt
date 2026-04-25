package com.team.prezel.core.network.datasource

import com.team.prezel.core.datastore.auth.AuthTokenStore
import com.team.prezel.core.model.auth.AuthToken
import javax.inject.Inject

internal class AuthLocalDataSourceImpl @Inject constructor(
    private val authTokenStore: AuthTokenStore,
) : AuthLocalDataSource {
    override suspend fun getToken(): AuthToken? = authTokenStore.getToken()

    override suspend fun saveToken(token: AuthToken) {
        authTokenStore.saveToken(token)
    }

    override suspend fun clear() {
        authTokenStore.clear()
    }
}
