package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.user.GetUserResponse
import java.io.File

interface UserRemoteDataSource {
    suspend fun getUser(): GetUserResponse

    suspend fun patchProfile(
        nickname: String,
        profileImageFile: File?,
    )

    suspend fun checkNickname(nickname: String): Boolean
}
