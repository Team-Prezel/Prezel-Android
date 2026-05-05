package com.team.prezel.core.domain.usecase.auth

import com.team.prezel.core.common.error.AppError
import com.team.prezel.core.common.error.AppException
import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.domain.repository.profile.UserRepository
import com.team.prezel.core.model.profile.User
import javax.inject.Inject

class CheckLoginStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<User?> =
        authRepository.hasJwtToken().fold(
            onSuccess = { isLoggedIn ->
                if (!isLoggedIn) return Result.success(null)
                fetchUserInfo()
            },
            onFailure = { throwable -> Result.failure(throwable) },
        )

    private suspend fun fetchUserInfo(): Result<User?> =
        userRepository.fetchUserInfo().fold(
            onSuccess = { user -> Result.success(user) },
            onFailure = { throwable ->
                if (!throwable.isUnauthenticatedUserError()) return Result.failure(throwable)

                authRepository.clearSession().fold(
                    onSuccess = { Result.success(null) },
                    onFailure = { clearSessionError -> Result.failure(clearSessionError) },
                )
            },
        )

    private fun Throwable.isUnauthenticatedUserError(): Boolean = (this as? AppException)?.error == AppError.UNAUTHORIZED
}
