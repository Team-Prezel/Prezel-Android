package com.team.prezel.core.domain.usecase

import com.team.prezel.core.domain.AuthRepository
import javax.inject.Inject

class CheckLoginStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Boolean {
        val accessToken = authRepository.getAccessToken()
        if (!accessToken.isNullOrBlank()) return true

        val refreshToken = authRepository.getRefreshToken()
        if (refreshToken.isNullOrBlank()) return false

        return authRepository.reissueToken(refreshToken).isSuccess
    }
}
