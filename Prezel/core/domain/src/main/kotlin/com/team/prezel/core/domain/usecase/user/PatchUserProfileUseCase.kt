package com.team.prezel.core.domain.usecase.user

import com.team.prezel.core.domain.repository.profile.UserRepository
import javax.inject.Inject

class PatchUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        nickname: String,
        profileImageBytes: ByteArray?,
        mimeType: String?,
    ): Result<Unit> =
        userRepository.patchProfile(
            nickname = nickname,
            profileImageBytes = profileImageBytes,
            mimeType = mimeType,
        )
}
