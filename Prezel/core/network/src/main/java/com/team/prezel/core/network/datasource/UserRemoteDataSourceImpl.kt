package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.requireData
import com.team.prezel.core.network.model.requireSuccess
import com.team.prezel.core.network.model.user.GetUserResponse
import com.team.prezel.core.network.service.UserService
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import io.ktor.utils.io.ByteReadChannel
import javax.inject.Inject

internal class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService,
) : UserRemoteDataSource {
    override suspend fun getUser(): GetUserResponse = userService.getUser().requireData()

    override suspend fun patchProfile(
        nickname: String,
        profileImageBytes: ByteArray?,
        mimeType: String?,
    ) {
        if (profileImageBytes == null) {
            userService.patchProfile(nickname = nickname).requireSuccess()
            return
        }
        userService
            .patchProfile(
                nickname = nickname,
                profileImage = profileImageBytes.toPartData(
                    mimeType = requireNotNull(mimeType) { "mimeType is required when profileImageBytes is provided." },
                ),
            ).requireSuccess()
    }

    override suspend fun checkNickname(nickname: String): Boolean = userService.checkNickname(nickname = nickname).requireData()

    private fun ByteArray.toPartData(mimeType: String): PartData =
        PartData.FileItem(
            provider = { ByteReadChannel(this@toPartData) },
            dispose = {},
            partHeaders = Headers.build {
                append(HttpHeaders.ContentType, mimeType)
                append(
                    HttpHeaders.ContentDisposition,
                    "form-data; name=\"profileImage\"; filename=\"${System.currentTimeMillis()}${mimeType.toFileExtension()}\"",
                )
            },
        )

    private fun String.toFileExtension(): String = lowercase().replace("image/", ".")
}
