package com.team.prezel.core.domain.usecase.auth

import com.team.prezel.core.domain.repository.auth.AuthRepository
import javax.inject.Inject

/**
 * 현재 로그인 세션의 로그아웃을 요청하는 UseCase.
 *
 * ### 동작 흐름
 * 1. [com.team.prezel.core.domain.repository.auth.AuthRepository.logout]을 호출합니다.
 * 2. repository가 저장된 토큰 조회와 서버 로그아웃 요청을 처리합니다.
 * 3. 결과를 [Result]로 반환합니다.
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<Unit> = authRepository.logout()
}
