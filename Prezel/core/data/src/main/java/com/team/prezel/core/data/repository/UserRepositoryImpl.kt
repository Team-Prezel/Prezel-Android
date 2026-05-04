package com.team.prezel.core.data.repository

import com.team.prezel.core.data.error.mapDomainFailure
import com.team.prezel.core.domain.repository.profile.UserRepository
import com.team.prezel.core.model.profile.Nickname
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.network.datasource.UserRemoteDataSource
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
) : UserRepository {
    private var cachedUserInfo: User? = null

    override suspend fun fetchUserInfo(isRefresh: Boolean): Result<User> =
        runCatching {
            if (!isRefresh && cachedUserInfo != null) return Result.success(cachedUserInfo!!)
            userRemoteDataSource.getUser()
        }.mapCatching { response ->
            with(response) {
                User(
                    id = id.toLong(),
                    email = email,
                    nickname = nickname.orEmpty(),
                    profileImageUrl = profileImgUrl.url?.takeIf { it.isNotBlank() },
                    isProfileComplete = isProfileComplete,
                    isTermsAgreement = isTermsAgreement,
                )
            }
        }.onSuccess { user ->
            cachedUserInfo = user
        }.mapDomainFailure()

    override suspend fun patchProfile(
        nickname: String,
        profileImageBytes: ByteArray?,
        mimeType: String?,
    ): Result<Unit> =
        runCatching {
            userRemoteDataSource.patchProfile(
                nickname = nickname,
                profileImageBytes = profileImageBytes,
                mimeType = mimeType,
            )
            cachedUserInfo = null
        }.mapDomainFailure()

    override suspend fun checkNicknameDuplication(nickname: Nickname): Result<Boolean> =
        runCatching {
            userRemoteDataSource.checkNickname(nickname = nickname.value)
        }.mapDomainFailure()
}
