package com.team.prezel.core.domain.usecase

import com.team.prezel.core.domain.AuthRepository
import com.team.prezel.core.model.auth.AuthToken
import javax.inject.Inject

/**
 * 소셜 로그인에 사용되는 ID 토큰으로 서버 로그인을 수행하는 UseCase.
 *
 * ### 동작 흐름
 * 1. 호출부로부터 전달받은 ID 토큰을 입력값으로 받습니다.
 * 2. [com.team.prezel.core.domain.repository.auth.AuthRepository.login]을 호출하여 서버 로그인 요청을 수행합니다.
 * 3. 로그인 결과에 따라 [AuthToken] 또는 예외를 포함한 [Result]를 반환합니다.
 *
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(idToken: String): Result<AuthToken> = authRepository.login(idToken = idToken)
}
