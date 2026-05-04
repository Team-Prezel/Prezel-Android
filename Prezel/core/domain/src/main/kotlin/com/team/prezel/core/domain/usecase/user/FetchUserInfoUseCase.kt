package com.team.prezel.core.domain.usecase.user

import com.team.prezel.core.domain.repository.profile.UserRepository
import com.team.prezel.core.model.profile.User
import javax.inject.Inject

/**
 * 유저 데이터를 조회하는 UseCase.
 */
class FetchUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<User> = userRepository.fetchUserInfo()
}
