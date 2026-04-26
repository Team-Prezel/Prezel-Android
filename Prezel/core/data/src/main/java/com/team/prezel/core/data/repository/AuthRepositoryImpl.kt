package com.team.prezel.core.data.repository

import com.team.prezel.core.datastore.auth.AuthLocalDataSource
import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.domain.result.auth.AuthActionResult
import com.team.prezel.core.domain.result.auth.LoginStatusResult
import com.team.prezel.core.model.auth.AuthToken
import com.team.prezel.core.model.auth.WithdrawReason
import com.team.prezel.core.network.datasource.AuthRemoteDataSource
import com.team.prezel.core.network.model.ApiResponse
import com.team.prezel.core.network.model.auth.LoginResponse
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) : AuthRepository {
    override suspend fun checkLoginStatus(): LoginStatusResult {
        val token = authLocalDataSource.getToken().first()
        return if (token == null) LoginStatusResult.Unauthenticated else LoginStatusResult.Authenticated
    }

    override suspend fun logout(): AuthActionResult {
        if (authLocalDataSource
                .getToken()
                .first()
                ?.accessToken
                .isNullOrBlank()
        ) {
            return clearTokensAndAuthenticationRequired()
        }

        return when (val response = authRemoteDataSource.logout()) {
            is ApiResponse.Success -> clearTokens().toAuthActionSuccessResult()

            is ApiResponse.Failure.HttpError -> response.toAuthActionResult()
            is ApiResponse.Failure.NetworkError -> AuthActionResult.Failure(response.throwable)
        }
    }

    override suspend fun login(idToken: String): Result<Unit> =
        when (val response = authRemoteDataSource.login(idToken = idToken)) {
            is ApiResponse.Success -> saveTokens(response.data)
            is ApiResponse.Failure.HttpError -> Result.failure(response.throwable)
            is ApiResponse.Failure.NetworkError -> Result.failure(response.throwable)
        }

    override suspend fun withdraw(reason: WithdrawReason): AuthActionResult {
        if (authLocalDataSource
                .getToken()
                .first()
                ?.accessToken
                .isNullOrBlank()
        ) {
            return clearTokensAndAuthenticationRequired()
        }

        return when (
            val response =
                authRemoteDataSource.withdraw(
                    reasonCategory = reason.toCategory(),
                    reasonText = reason.toReasonText(),
                )
        ) {
            is ApiResponse.Success -> clearTokens().toAuthActionSuccessResult()

            is ApiResponse.Failure.HttpError -> response.toAuthActionResult()
            is ApiResponse.Failure.NetworkError -> AuthActionResult.Failure(response.throwable)
        }
    }

    private suspend fun saveTokens(response: LoginResponse): Result<Unit> {
        val token = response.toAuthToken()
        return authLocalDataSource.saveToken(token)
    }

    private fun LoginResponse.toAuthToken(): AuthToken =
        AuthToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )

    private suspend fun ApiResponse.Failure.HttpError.toAuthActionResult(): AuthActionResult =
        if (error?.code == AUTHENTICATION_REQUIRED_CODE) {
            clearTokens().fold(
                onSuccess = { AuthActionResult.AuthenticationRequired },
                onFailure = { throwable -> AuthActionResult.Failure(throwable) },
            )
        } else {
            AuthActionResult.Failure(throwable)
        }

    private suspend fun clearTokensAndAuthenticationRequired(): AuthActionResult =
        clearTokens().fold(
            onSuccess = { AuthActionResult.AuthenticationRequired },
            onFailure = { throwable -> AuthActionResult.Failure(throwable) },
        )

    private suspend fun clearTokens(): Result<Unit> = authLocalDataSource.clear()

    private fun Result<Unit>.toAuthActionSuccessResult(): AuthActionResult =
        fold(
            onSuccess = { AuthActionResult.Success },
            onFailure = { throwable -> AuthActionResult.Failure(throwable) },
        )

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

    private companion object {
        const val AUTHENTICATION_REQUIRED_CODE = "U001"
    }
}
