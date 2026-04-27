package com.team.prezel.core.data.repository

import com.team.prezel.core.common.di.ApplicationScope
import com.team.prezel.core.datastore.auth.AuthLocalDataSource
import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.model.auth.LoginStatus
import com.team.prezel.core.model.auth.WithdrawReason
import com.team.prezel.core.network.datasource.AuthRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

internal class DefaultAuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
    @param:ApplicationScope private val externalScope: CoroutineScope,
) : AuthRepository {
    override val loginStatus: StateFlow<LoginStatus> =
        authLocalDataSource.tokens
            .map { tokens -> if (tokens == null) LoginStatus.UNAUTHENTICATED else LoginStatus.AUTHENTICATED }
            .stateIn(
                scope = externalScope,
                started = SharingStarted.Eagerly,
                initialValue = LoginStatus.LOADING,
            )

    override suspend fun logout(): Result<Unit> =
        runCatching {
            authRemoteDataSource.logout()
            authLocalDataSource.clearTokens()
        }

    override suspend fun login(idToken: String): Result<Unit> =
        runCatching {
            val response = authRemoteDataSource.login(idToken = idToken)
            authLocalDataSource.saveTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
            )
        }

    override suspend fun withdraw(reason: WithdrawReason): Result<Unit> =
        runCatching {
            authRemoteDataSource.withdraw(
                reasonCategory = reason.toCategory(),
                reasonText = reason.toReasonText(),
            )
            authLocalDataSource.clearTokens()
        }

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
}
