package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.requireData
import com.team.prezel.core.network.model.requireSuccess
import com.team.prezel.core.network.model.user.GetUserResponse
import com.team.prezel.core.network.service.UserService
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File
import javax.inject.Inject

internal class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService,
) : UserRemoteDataSource {
    override suspend fun getUser(): GetUserResponse = userService.getUser().requireData()

    override suspend fun patchProfile(
        nickname: String,
        profileImageFile: File?,
    ) {
        val multipart = MultiPartFormDataContent(
            formData {
                append("\"nickname\"", nickname)
                profileImageFile?.let { file ->
                    append(
                        "\"profileImage\"",
                        file.readBytes(),
                        Headers.build {
                            append(HttpHeaders.ContentType, "image/${file.extension}")
                            append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                        },
                    )
                } ?: append("\"deleteImage\"", true)
            },
        )

        userService.patchProfile(multipart).requireSuccess()
    }

    override suspend fun checkNickname(nickname: String): Boolean = userService.checkNickname(nickname = nickname).requireData()
}
