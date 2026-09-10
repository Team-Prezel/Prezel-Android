package com.team.prezel.core.domain.usecase.fcm

import com.team.prezel.core.domain.repository.fcm.FcmTokenProvider
import javax.inject.Inject

class RegisterFcmTokenUseCase @Inject constructor(
    private val fcmTokenProvider: FcmTokenProvider,
    private val saveFcmTokenUseCase: SaveFcmTokenUseCase,
) {
    suspend operator fun invoke(): Result<Unit> =
        fcmTokenProvider.getToken().fold(
            onSuccess = { token -> saveFcmTokenUseCase(token = token) },
            onFailure = { exception -> Result.failure(exception) },
        )
}
