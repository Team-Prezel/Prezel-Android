package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.user.GetUserResponse

interface UserRemoteDataSource {
    suspend fun getUser(): GetUserResponse

    suspend fun patchProfile(
        nickname: String,
        profileImageBytes: ByteArray?,
        mimeType: String?,
    )

    suspend fun checkNickname(nickname: String): Boolean
}
