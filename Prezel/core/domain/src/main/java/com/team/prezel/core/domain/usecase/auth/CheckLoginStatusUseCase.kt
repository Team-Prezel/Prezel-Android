package com.team.prezel.core.domain.usecase.auth

import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.model.auth.LoginStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 저장된 인증 토큰을 기반으로 현재 로그인 상태를 확인하는 UseCase.
 *
 * ### 동작 흐름
 * 1. [com.team.prezel.core.domain.repository.auth.AuthRepository.loginStatus]를 구독합니다.
 * 2. 저장된 토큰을 기반으로 로그인 상태 판별은 repository 내부에서 처리합니다.
 * 3. 판별 결과를 [Flow] 형태로 반환합니다.
 *
 */
class CheckLoginStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<LoginStatus> = authRepository.loginStatus
}
