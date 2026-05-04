package com.team.prezel.core.data.repository

import com.team.prezel.core.data.error.mapDomainFailure
import com.team.prezel.core.data.mapper.toUser
import com.team.prezel.core.datastore.auth.AuthLocalDataSource
import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.model.auth.WithdrawReason
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.network.auth.AuthSessionCache
import com.team.prezel.core.network.datasource.AuthRemoteDataSource
import com.team.prezel.core.network.datasource.UserRemoteDataSource
import com.team.prezel.core.network.model.ApiException
import com.team.prezel.core.network.model.ServerErrorCode
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
    private val authSessionCache: AuthSessionCache,
) : AuthRepository {
    override suspend fun checkLoginStatus(): Result<User?> = fetchCurrentUser()

    override suspend fun logout(): Result<Unit> =
        runCatching {
            authRemoteDataSource.logout()
            authLocalDataSource.clearTokens()
            authSessionCache.clear()
        }.mapDomainFailure()

    override suspend fun login(idToken: String): Result<User?> =
        runCatching {
            val response = authRemoteDataSource.login(idToken = idToken)
            authLocalDataSource.saveTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
            )
            authSessionCache.clear()
        }.fold(
            onSuccess = { fetchCurrentUser() },
            onFailure = { throwable -> Result.failure<User?>(throwable).mapDomainFailure() },
        )

    override suspend fun withdraw(reason: WithdrawReason): Result<Unit> =
        runCatching {
            authRemoteDataSource.withdraw(
                reasonCategory = reason.toCategory(),
                reasonText = reason.toReasonText(),
            )
            authLocalDataSource.clearTokens()
            authSessionCache.clear()
        }.mapDomainFailure()

    private fun WithdrawReason.toCategory(): String =
        when (this) {
            WithdrawReason.NotUsedOften -> "NOT_USED_OFTEN"
            WithdrawReason.NoLongerNeeded -> "NO_LONGER_NEEDED"
            WithdrawReason.TooDifficultOrComplex -> "TOO_COMPLEX"
            WithdrawReason.AnalysisResultInaccurate -> "INACCURATE_ANALYSIS"
            WithdrawReason.TooManyErrors -> "MANY_ERRORS"
            is WithdrawReason.Other -> "ETC"
        }

    private fun WithdrawReason.toReasonText(): String =
        when (this) {
            is WithdrawReason.Other -> text
            else -> ""
        }

    private suspend fun fetchCurrentUser(): Result<User?> {
        if (authLocalDataSource.tokens.firstOrNull() == null) return Result.success(null)

        return try {
            Result.success(userRemoteDataSource.getUser().toUser())
        } catch (throwable: Throwable) {
            if ((throwable as? ApiException)?.errorCode in listOf(ServerErrorCode.UNAUTHORIZED, ServerErrorCode.USER_NOT_FOUND)) {
                authLocalDataSource.clearTokens()
                authSessionCache.clear()
                Result.success(null)
            } else {
                Result.failure<User?>(throwable).mapDomainFailure()
            }
        }
    }
}
