package com.team.prezel.core.domain.usecase.auth

import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.domain.result.auth.LoginStatusResult
import javax.inject.Inject

/**
 * 저장된 인증 토큰을 기반으로 현재 로그인 상태를 확인하는 UseCase.
 *
 * ### 동작 흐름
 * 1. [com.team.prezel.core.domain.repository.auth.AuthRepository.checkLoginStatus]를 호출합니다.
 * 2. 저장된 토큰 및 재발급 여부를 포함한 로그인 상태 판별은 repository 내부에서 처리합니다.
 * 3. 판별 결과로 [LoginStatusResult]를 반환합니다.
 *
 */
class CheckLoginStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): LoginStatusResult = authRepository.checkLoginStatus()
}
