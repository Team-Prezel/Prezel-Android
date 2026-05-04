package com.team.prezel.core.domain.usecase.user

import com.team.prezel.core.domain.repository.profile.UserRepository
import java.io.File
import javax.inject.Inject

class PatchUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        nickname: String,
        profileImageFile: File?,
    ): Result<Unit> =
        userRepository.patchProfile(
            nickname = nickname,
            profileImageFile = profileImageFile,
        )
}
