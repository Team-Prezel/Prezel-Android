package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.fcm.FcmTokenRequest
import com.team.prezel.core.network.model.requireSuccess
import com.team.prezel.core.network.service.FcmService
import javax.inject.Inject

internal class FcmRemoteDataSourceImpl @Inject constructor(
    private val fcmService: FcmService,
) : FcmRemoteDataSource {
    override suspend fun saveToken(token: String) {
        fcmService.saveToken(request = FcmTokenRequest(token = token)).requireSuccess()
    }

    override suspend fun removeToken(token: String) {
        fcmService.removeToken(request = FcmTokenRequest(token = token)).requireSuccess()
    }
}
