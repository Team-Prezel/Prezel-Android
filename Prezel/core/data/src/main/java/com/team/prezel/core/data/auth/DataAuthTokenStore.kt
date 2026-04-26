package com.team.prezel.core.data.auth

import com.team.prezel.core.datastore.auth.AuthLocalDataSource
import com.team.prezel.core.model.auth.AuthToken
import com.team.prezel.core.network.auth.AuthTokenStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DataAuthTokenStore @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource,
) : AuthTokenStore {
    override fun getToken(): Flow<AuthToken?> = authLocalDataSource.getToken()

    override suspend fun saveToken(token: AuthToken): Result<Unit> = authLocalDataSource.saveToken(token)

    override suspend fun clear(): Result<Unit> = authLocalDataSource.clear()
}
