package com.team.prezel.core.domain.usecase.user

import com.team.prezel.core.domain.repository.profile.UserRepository
import javax.inject.Inject

/**
 * 캐시된 사용자 닉네임을 우선 조회하는 UseCase.
 */
class GetUserNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<String> = userRepository.getUserNickname()
}
