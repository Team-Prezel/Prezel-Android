package com.team.prezel.core.domain.usecase.auth

import com.team.prezel.core.domain.repository.auth.AuthRepository
import javax.inject.Inject

/**
 * 저장된 인증 토큰을 기반으로 현재 로그인 상태를 확인하는 UseCase.
 *
 * ### 동작 흐름
 * 1. [com.team.prezel.core.domain.repository.auth.AuthRepository.awaitTokenStoreInitialized]를 호출해
 *    토큰 저장소의 초기화 완료를 보장합니다.
 * 2. 저장된 액세스 토큰이 존재하면 로그인 상태로 판단하여 `true`를 반환합니다.
 * 3. 액세스 토큰이 없으면 저장된 리프레시 토큰 존재 여부를 확인합니다.
 * 4. 리프레시 토큰도 없으면 `false`를 반환합니다.
 * 5. 리프레시 토큰이 있으면 [com.team.prezel.core.domain.repository.auth.AuthRepository.reissueToken]을 호출해
 *    재발급을 시도하고, 성공 여부를 반환합니다.
 *
 */
class CheckLoginStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Boolean {
        authRepository.awaitTokenStoreInitialized()

        val accessToken = authRepository.getAccessToken()
        if (!accessToken.isNullOrBlank()) return true

        val refreshToken = authRepository.getRefreshToken()
        if (refreshToken.isNullOrBlank()) return false

        return authRepository.reissueToken(refreshToken).isSuccess
    }
}
