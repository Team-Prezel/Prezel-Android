package com.team.prezel.core.data.repository

import com.team.prezel.core.common.extensions.toSnakeCase
import com.team.prezel.core.data.error.mapDomainFailure
import com.team.prezel.core.datastore.auth.AuthLocalDataSource
import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.model.auth.WithdrawReason
import com.team.prezel.core.network.auth.AuthSessionCache
import com.team.prezel.core.network.datasource.AuthRemoteDataSource
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
    private val authSessionCache: AuthSessionCache,
) : AuthRepository {
    override suspend fun hasJwtToken(): Result<Boolean> = Result.success(authLocalDataSource.tokens.firstOrNull() != null)

    override suspend fun logout(): Result<Unit> =
        runCatching {
            authRemoteDataSource.logout()
            clearLocalSession()
        }.mapDomainFailure()

    override suspend fun login(idToken: String): Result<Unit> =
        runCatching {
            val response = authRemoteDataSource.login(idToken = idToken)
            authLocalDataSource.saveTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
            )
            authSessionCache.clear()
        }.mapDomainFailure()

    override suspend fun clearSession(): Result<Unit> = runCatching { clearLocalSession() }.mapDomainFailure()

    override suspend fun withdraw(reason: WithdrawReason): Result<Unit> =
        runCatching {
            authRemoteDataSource.withdraw(
                reasonCategory = reason.javaClass.simpleName.toSnakeCase(),
                reasonText = (reason as? WithdrawReason.Etc)?.text,
            )
            clearLocalSession()
        }.mapDomainFailure()

    private suspend fun clearLocalSession() {
        authLocalDataSource.clearTokens()
        authSessionCache.clear()
    }
}
