package com.team.prezel.core.network.service

import com.team.prezel.core.network.model.BaseResponse
import com.team.prezel.core.network.model.user.GetUserResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.Part
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.http.content.PartData

interface UserService {
    @GET("user")
    suspend fun getUser(): BaseResponse<GetUserResponse>

    @PATCH("user/profile")
    suspend fun patchProfile(
        @Part("nickname") nickname: String,
    ): BaseResponse<Unit>

    @PATCH("user/profile")
    @Multipart
    suspend fun patchProfile(
        @Part("nickname") nickname: String,
        @Part("profileImage") profileImage: PartData,
    ): BaseResponse<Unit>

    @GET("user/check-nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname: String,
    ): BaseResponse<Boolean>
}
