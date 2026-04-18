package com.team.prezel.core.domain.usecase.auth

import com.team.prezel.core.domain.repository.auth.AuthRepository
import com.team.prezel.core.domain.result.auth.AuthActionResult
import com.team.prezel.core.model.auth.WithdrawReason
import javax.inject.Inject

/**
 * 회원 탈퇴 사유와 함께 탈퇴 요청을 수행하는 UseCase.
 *
 * ### 동작 흐름
 * 1. 호출부로부터 전달받은 [WithdrawReason]으로 [com.team.prezel.core.domain.repository.auth.AuthRepository.withdraw]를 호출합니다.
 * 2. repository가 저장된 토큰 조회와 서버 회원 탈퇴 요청을 처리합니다.
 * 3. 결과를 [AuthActionResult]로 반환합니다.
 */
class WithdrawUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(reason: WithdrawReason): AuthActionResult = authRepository.withdraw(reason = reason)
}
