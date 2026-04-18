package com.team.prezel.core.domain.usecase.auth

import com.team.prezel.core.domain.repository.auth.AuthRepository
import javax.inject.Inject

/**
 * 현재 로그인 세션의 로그아웃을 요청하는 UseCase.
 *
 * ### 동작 흐름
 * 1. 저장된 현재 액세스 토큰을 조회합니다.
 * 2. [com.team.prezel.core.domain.repository.auth.AuthRepository.logout]을 호출하여 서버에 로그아웃을 요청합니다.
 * 3. 요청 결과에 따라 성공 여부를 [Result]로 반환합니다.
 *
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        val accessToken =
            authRepository.getAccessToken() ?: return Result.failure(
                IllegalStateException("저장된 access token이 없습니다."),
            )

        return authRepository.logout(accessToken = accessToken)
    }
}
