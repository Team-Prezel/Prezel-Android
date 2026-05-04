package com.team.prezel.core.network.service

import com.team.prezel.core.network.model.BaseResponse
import com.team.prezel.core.network.model.user.GetUserResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.request.forms.MultiPartFormDataContent

interface UserService {
    @GET("user")
    suspend fun getUser(): BaseResponse<GetUserResponse>

    @PATCH("user/profile")
    suspend fun patchProfile(
        @Body map: MultiPartFormDataContent,
    ): BaseResponse<Unit>

    @GET("user/check-nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname: String,
    ): BaseResponse<Boolean>
}
