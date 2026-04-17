package com.team.prezel.core.domain.usecase

import com.team.prezel.core.domain.AuthRepository
import com.team.prezel.core.model.auth.AuthToken
import javax.inject.Inject

/**
 * 리프레시 토큰을 기반으로 인증 토큰 재발급을 요청하는 UseCase.
 *
 * ### 동작 흐름
 * 1. 호출부로부터 전달받은 리프레시 토큰을 입력값으로 받습니다.
 * 2. [com.team.prezel.core.domain.repository.auth.AuthRepository.reissueToken]을 호출하여 서버에 토큰 재발급을 요청합니다.
 * 3. 재발급 결과에 따라 새로운 [AuthToken] 또는 예외를 포함한 [Result]를 반환합니다.
 *
 */
class ReissueTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(refreshToken: String): Result<AuthToken> = authRepository.reissueToken(refreshToken = refreshToken)
}
