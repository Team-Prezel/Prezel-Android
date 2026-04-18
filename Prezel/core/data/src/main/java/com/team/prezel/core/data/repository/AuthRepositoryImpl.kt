package com.team.prezel.core.data.repository

import com.team.prezel.core.data.toResult
import com.team.prezel.core.datastore.auth.AuthTokenStore
import com.team.prezel.core.domain.error.AuthenticationRequiredException
import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.model.auth.AuthToken
import com.team.prezel.core.model.auth.WithdrawReason
import com.team.prezel.core.network.datasource.AuthRemoteDataSource
import com.team.prezel.core.network.model.ApiResponse
import com.team.prezel.core.network.model.auth.LoginResponse
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authTokenStore: AuthTokenStore,
) : AuthRepository {
    override fun getAccessToken(): String? = authTokenStore.getAccessToken()

    override fun getRefreshToken(): String? = authTokenStore.getRefreshToken()

    override suspend fun reissueToken(refreshToken: String): Result<AuthToken> =
        authRemoteDataSource.reissueToken(refreshToken = refreshToken).toResult(::saveTokens)

    override suspend fun logout(): Result<Unit> {
        val accessToken = authTokenStore.getAccessToken() ?: return clearTokensAndAuthenticationRequired()

        return when (val response = authRemoteDataSource.logout(accessToken = accessToken)) {
            is ApiResponse.Success -> Result.success(authTokenStore.clear())
            is ApiResponse.Failure.HttpError -> response.toLogoutResult()
            is ApiResponse.Failure.NetworkError -> Result.failure(response.throwable)
        }
    }

    override suspend fun login(idToken: String): Result<AuthToken> =
        authRemoteDataSource.login(idToken = idToken).toResult(::saveTokens)

    override suspend fun withdraw(reason: WithdrawReason): Result<Unit> {
        val accessToken = authTokenStore.getAccessToken() ?: return clearTokensAndAuthenticationRequired()

        return when (
            val response =
                authRemoteDataSource.withdraw(
                    accessToken = accessToken,
                    reasonCategory = reason.toCategory(),
                    reasonText = reason.toReasonText(),
                )
        ) {
            is ApiResponse.Success -> Result.success(authTokenStore.clear())
            is ApiResponse.Failure.HttpError -> response.toLogoutResult()
            is ApiResponse.Failure.NetworkError -> Result.failure(response.throwable)
        }
    }

    private suspend fun saveTokens(response: LoginResponse): AuthToken =
        response
            .toAuthToken()
            .also { token ->
                authTokenStore.saveTokens(
                    accessToken = token.accessToken,
                    refreshToken = token.refreshToken,
                )
            }

    private fun LoginResponse.toAuthToken(): AuthToken =
        AuthToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )

    private suspend fun ApiResponse.Failure.HttpError.toLogoutResult(): Result<Unit> =
        if (error?.code == AUTHENTICATION_REQUIRED_CODE) {
            authTokenStore.clear()
            authenticationRequired(message = error?.message)
        } else {
            Result.failure(throwable)
        }

    private fun authenticationRequired(message: String? = null): Result<Unit> =
        Result.failure(AuthenticationRequiredException(message ?: "인증이 필요합니다."))

    private suspend fun clearTokensAndAuthenticationRequired(): Result<Unit> {
        authTokenStore.clear()
        return authenticationRequired()
    }

    private fun WithdrawReason.toCategory(): String =
        when (this) {
            WithdrawReason.NotUsedOften -> "NOT_USED_OFTEN"
            WithdrawReason.NoLongerNeeded -> "NO_LONGER_NEEDED"
            WithdrawReason.TooDifficultOrComplex -> "TOO_DIFFICULT_OR_COMPLEX"
            WithdrawReason.AnalysisResultInaccurate -> "ANALYSIS_RESULT_INACCURATE"
            WithdrawReason.TooManyErrors -> "TOO_MANY_ERRORS"
            is WithdrawReason.Other -> "OTHER"
        }

    private fun WithdrawReason.toReasonText(): String =
        when (this) {
            is WithdrawReason.Other -> text
            else -> ""
        }

    private companion object {
        const val AUTHENTICATION_REQUIRED_CODE = "U001"
    }
}
