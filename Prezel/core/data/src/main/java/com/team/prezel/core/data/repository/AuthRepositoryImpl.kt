package com.team.prezel.core.data.repository

import com.team.prezel.core.common.di.ApplicationScope
import com.team.prezel.core.data.error.mapDomainFailure
import com.team.prezel.core.datastore.auth.AuthLocalDataSource
import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.model.auth.AuthCheckResult
import com.team.prezel.core.model.auth.WithdrawReason
import com.team.prezel.core.network.auth.AuthSessionCache
import com.team.prezel.core.network.datasource.AuthRemoteDataSource
import com.team.prezel.core.network.datasource.UserRemoteDataSource
import com.team.prezel.core.network.model.ApiException
import com.team.prezel.core.network.model.ServerErrorCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
    private val authSessionCache: AuthSessionCache,
    @param:ApplicationScope private val externalScope: CoroutineScope,
) : AuthRepository {
    override val authCheckResult: StateFlow<AuthCheckResult> =
        authLocalDataSource.tokens
            .mapLatest(::resolveAuthCheckResult)
            .stateIn(
                scope = externalScope,
                started = SharingStarted.Eagerly,
                initialValue = AuthCheckResult.Loading,
            )

    override suspend fun logout(): Result<Unit> =
        runCatching {
            authRemoteDataSource.logout()
            authLocalDataSource.clearTokens()
            authSessionCache.clear()
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

    private suspend fun resolveAuthCheckResult(tokens: AuthLocalDataSource.AuthTokens?): AuthCheckResult {
        if (tokens == null) return AuthCheckResult.Unauthenticated

        return runCatching {
            userRemoteDataSource.getUser()
        }.fold(
            onSuccess = { user ->
                when {
                    !user.isTermsAgreement -> AuthCheckResult.NeedsTermsAgreement
                    !user.isProfileComplete -> AuthCheckResult.NeedsProfileCompletion
                    else -> AuthCheckResult.Authenticated
                }
            },
            onFailure = { throwable ->
                if ((throwable as? ApiException)?.errorCode in listOf(ServerErrorCode.UNAUTHORIZED, ServerErrorCode.USER_NOT_FOUND)) {
                    authLocalDataSource.clearTokens()
                    authSessionCache.clear()
                    AuthCheckResult.Unauthenticated
                } else {
                    AuthCheckResult.RetryableFailure
                }
            },
        )
    }
}
