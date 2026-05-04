package com.team.prezel.core.data.repository

import com.team.prezel.core.data.error.mapDomainFailure
import com.team.prezel.core.domain.repository.profile.UserRepository
import com.team.prezel.core.model.profile.Nickname
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.network.datasource.UserRemoteDataSource
import com.team.prezel.core.network.model.user.GetUserResponse
import java.io.File
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
) : UserRepository {
    override suspend fun fetchUserInfo(): Result<User> =
        runCatching {
            userRemoteDataSource.getUser()
        }.mapCatching { response ->
            response.toDomain()
        }.mapDomainFailure()

    override suspend fun patchProfile(
        nickname: String,
        profileImageFile: File?,
    ): Result<Unit> =
        runCatching {
            userRemoteDataSource.patchProfile(
                nickname = nickname,
                profileImageFile = profileImageFile,
            )
        }.mapDomainFailure()

    override suspend fun checkNicknameDuplication(nickname: Nickname): Result<Boolean> =
        runCatching {
            userRemoteDataSource.checkNickname(nickname = nickname.value)
        }.mapDomainFailure()

    private fun GetUserResponse.toDomain(): User =
        User(
            id = id,
            email = email,
            nickname = nickname.orEmpty(),
            profileImageUrl = profileImgUrl.url?.takeIf { url -> url.isNotBlank() },
            isProfileComplete = isProfileComplete,
            isTermsAgreement = isTermsAgreement,
        )
}
