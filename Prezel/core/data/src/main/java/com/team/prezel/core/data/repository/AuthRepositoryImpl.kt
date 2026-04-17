package com.team.prezel.core.data.repository

import com.team.prezel.core.data.toResult
import com.team.prezel.core.domain.AuthRepository
import com.team.prezel.core.model.auth.AuthToken
import com.team.prezel.core.model.auth.WithdrawReason
import com.team.prezel.core.network.datasource.AuthRemoteDataSource
import com.team.prezel.core.network.model.auth.LoginResponse
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
) : AuthRepository {
    override suspend fun reissueToken(refreshToken: String): Result<AuthToken> =
        authRemoteDataSource.reissueToken(refreshToken = refreshToken).toResult { it.toAuthToken() }

    override suspend fun logout(accessToken: String): Result<Unit> = authRemoteDataSource.logout(accessToken = accessToken).toResult { Unit }

    override suspend fun login(idToken: String): Result<AuthToken> = authRemoteDataSource.login(idToken = idToken).toResult { it.toAuthToken() }

    override suspend fun withdraw(
        accessToken: String,
        reason: WithdrawReason,
    ): Result<Unit> =
        authRemoteDataSource
            .withdraw(
                accessToken = accessToken,
                reasonCategory = reason.category,
                reasonText = reason.reasonText,
            ).toResult { Unit }

    private fun LoginResponse.toAuthToken(): AuthToken =
        AuthToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )

    private val WithdrawReason.category: String
        get() = when (this) {
            WithdrawReason.NotUsedOften -> "NOT_USED_OFTEN"
            WithdrawReason.NoLongerNeeded -> "NO_LONGER_NEEDED"
            WithdrawReason.TooDifficultOrComplex -> "TOO_DIFFICULT_OR_COMPLEX"
            WithdrawReason.AnalysisResultInaccurate -> "ANALYSIS_RESULT_INACCURATE"
            WithdrawReason.TooManyErrors -> "TOO_MANY_ERRORS"
            is WithdrawReason.Other -> "OTHER"
        }

    private val WithdrawReason.reasonText: String
        get() = when (this) {
            is WithdrawReason.Other -> text
            else -> ""
        }
}
