package com.team.prezel.core.data.repository

import com.team.prezel.core.data.toResult
import com.team.prezel.core.datastore.auth.AuthTokenStore
import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.domain.usecase.auth.AuthActionResult
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

    override suspend fun logout(): AuthActionResult {
        val accessToken = authTokenStore.getAccessToken() ?: return clearTokensAndAuthenticationRequired()

        return when (val response = authRemoteDataSource.logout(accessToken = accessToken)) {
            is ApiResponse.Success -> {
                authTokenStore.clear()
                AuthActionResult.Success
            }
            is ApiResponse.Failure.HttpError -> response.toAuthActionResult()
            is ApiResponse.Failure.NetworkError -> AuthActionResult.Failure(response.throwable)
        }
    }

    override suspend fun login(idToken: String): Result<AuthToken> = authRemoteDataSource.login(idToken = idToken).toResult(::saveTokens)

    override suspend fun withdraw(reason: WithdrawReason): AuthActionResult {
        val accessToken = authTokenStore.getAccessToken() ?: return clearTokensAndAuthenticationRequired()

        return when (
            val response =
                authRemoteDataSource.withdraw(
                    accessToken = accessToken,
                    reasonCategory = reason.toCategory(),
                    reasonText = reason.toReasonText(),
                )
        ) {
            is ApiResponse.Success -> {
                authTokenStore.clear()
                AuthActionResult.Success
            }
            is ApiResponse.Failure.HttpError -> response.toAuthActionResult()
            is ApiResponse.Failure.NetworkError -> AuthActionResult.Failure(response.throwable)
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

    private suspend fun ApiResponse.Failure.HttpError.toAuthActionResult(): AuthActionResult =
        if (error?.code == AUTHENTICATION_REQUIRED_CODE) {
            authTokenStore.clear()
            AuthActionResult.AuthenticationRequired
        } else {
            AuthActionResult.Failure(throwable)
        }

    private suspend fun clearTokensAndAuthenticationRequired(): AuthActionResult {
        authTokenStore.clear()
        return AuthActionResult.AuthenticationRequired
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
