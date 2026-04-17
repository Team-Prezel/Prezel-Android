package com.team.prezel.core.domain.usecase

import com.team.prezel.core.domain.AuthRepository
import javax.inject.Inject

/**
 * 현재 로그인 세션의 로그아웃을 요청하는 UseCase.
 *
 * ### 동작 흐름
 * 1. 호출부로부터 전달받은 액세스 토큰을 입력값으로 받습니다.
 * 2. [com.team.prezel.core.domain.repository.auth.AuthRepository.logout]을 호출하여 서버에 로그아웃을 요청합니다.
 * 3. 요청 결과에 따라 성공 여부를 [Result]로 반환합니다.
 *
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(accessToken: String): Result<Unit> = authRepository.logout(accessToken = accessToken)
}
