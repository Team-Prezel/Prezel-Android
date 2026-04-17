package com.team.prezel.core.domain.usecase

import com.team.prezel.core.domain.AuthRepository
import com.team.prezel.core.model.auth.WithdrawReason
import javax.inject.Inject

/**
 * 회원 탈퇴 사유와 함께 탈퇴 요청을 수행하는 UseCase.
 *
 * ### 동작 흐름
 * 1. 호출부로부터 전달받은 액세스 토큰과 [WithdrawReason]을 입력값으로 받습니다.
 * 2. [com.team.prezel.core.domain.repository.auth.AuthRepository.withdraw]를 호출하여 서버에 회원 탈퇴를 요청합니다.
 * 3. 요청 결과에 따라 성공 여부를 [Result]로 반환합니다.
 *
 */
class WithdrawUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        accessToken: String,
        reason: WithdrawReason,
    ): Result<Unit> =
        authRepository.withdraw(
            accessToken = accessToken,
            reason = reason,
        )
}

