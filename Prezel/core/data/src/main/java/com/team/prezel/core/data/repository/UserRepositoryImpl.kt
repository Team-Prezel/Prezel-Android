package com.team.prezel.core.data.repository

import com.team.prezel.core.domain.repository.profile.UserRepository
import com.team.prezel.core.model.profile.Nickname
import com.team.prezel.core.model.profile.User
import kotlinx.coroutines.delay
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor() : UserRepository {
    private var cachedUserInfo: User? = null

    override suspend fun fetchUserInfo(isRefresh: Boolean): Result<User> =
        runCatching {
            if (!isRefresh && cachedUserInfo != null) return Result.success(cachedUserInfo!!)

            User(
                id = 1,
                email = "test@gmail.com",
                nickname = "",
                profileImage = User.ProfileImage(url = "https://picsum.photos/200", isDefault = true),
                isRegistered = false,
            )
        }.onSuccess { user ->
            cachedUserInfo = user
        }

    override suspend fun checkNicknameDuplication(nickname: Nickname): Result<Boolean> {
        // TODO: 실제 API 연동 후 서버 중복 검사 결과를 반환하도록 교체
        delay(200)
        return Result.success(false)
    }
}
