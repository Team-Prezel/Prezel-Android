package com.team.prezel.core.domain.usecase.auth

import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.domain.repository.profile.UserRepository
import com.team.prezel.core.model.profile.User
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(idToken: String): Result<User> =
        authRepository.login(idToken = idToken).fold(
            onSuccess = { userRepository.fetchUserInfo() },
            onFailure = { exception -> Result.failure(exception) },
        )

    suspend fun loginAdmin(): Result<User> =
        authRepository.loginAdmin().fold(
            onSuccess = { userRepository.fetchUserInfo() },
            onFailure = { exception -> Result.failure(exception) },
        )
}
