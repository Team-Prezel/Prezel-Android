package com.team.prezel.core.network.service

import com.team.prezel.core.network.model.BaseResponse
import com.team.prezel.core.network.model.fcm.FcmTokenRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.POST

interface FcmService {
    @POST("v2/fcm/token")
    suspend fun saveToken(
        @Body request: FcmTokenRequest,
    ): BaseResponse<Unit>

    @DELETE("v2/fcm/token")
    suspend fun removeToken(
        @Body request: FcmTokenRequest,
    ): BaseResponse<Unit>
}
