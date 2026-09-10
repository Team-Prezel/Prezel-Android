package com.team.prezel.core.domain.usecase.fcm

import com.team.prezel.core.domain.repository.fcm.FcmRepository
import javax.inject.Inject

class SaveFcmTokenUseCase @Inject constructor(
    private val fcmRepository: FcmRepository,
) {
    suspend operator fun invoke(token: String): Result<Unit> = fcmRepository.saveToken(token = token)
}
