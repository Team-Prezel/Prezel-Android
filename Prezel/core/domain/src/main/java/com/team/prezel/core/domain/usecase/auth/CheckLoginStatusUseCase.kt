package com.team.prezel.core.domain.usecase.auth

import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.domain.result.auth.LoginStatusResult
import javax.inject.Inject

/**
 * 저장된 인증 토큰을 기반으로 현재 로그인 상태를 확인하는 UseCase.
 *
 * ### 동작 흐름
 * 1. [com.team.prezel.core.domain.repository.auth.AuthRepository.awaitTokenStoreInitialized]를 호출해
 *    토큰 저장소의 초기화 완료를 보장합니다.
 * 2. 저장된 액세스 토큰이 존재하면 [LoginStatusResult.Authenticated]를 반환합니다.
 * 3. 액세스 토큰이 없으면 저장된 리프레시 토큰 존재 여부를 확인합니다.
 * 4. 리프레시 토큰도 없으면 [LoginStatusResult.Unauthenticated]를 반환합니다.
 * 5. 리프레시 토큰이 있으면 [com.team.prezel.core.domain.repository.auth.AuthRepository.reissueToken]을 호출해
 *    재발급을 시도합니다.
 * 6. 재발급에 성공하면 [LoginStatusResult.Authenticated]를 반환합니다.
 * 7. 재발급이 인증 복구 불가로 실패하면 [LoginStatusResult.Unauthenticated]를 반환합니다.
 * 8. 재발급이 네트워크 오류 등 일시적인 실패로 끝나면 [LoginStatusResult.RetryableFailure]를 반환합니다.
 *
 */
class CheckLoginStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): LoginStatusResult {
        authRepository.awaitTokenStoreInitialized()

        val accessToken = authRepository.getAccessToken()
        if (!accessToken.isNullOrBlank()) return LoginStatusResult.Authenticated

        val refreshToken = authRepository.getRefreshToken()
        if (refreshToken.isNullOrBlank()) return LoginStatusResult.Unauthenticated

        return authRepository.reissueToken(refreshToken)
    }
}
