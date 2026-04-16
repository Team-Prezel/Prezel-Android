package com.team.prezel.core.domain.repository.profile

import com.team.prezel.core.model.profile.Nickname
import com.team.prezel.core.model.profile.User

interface UserRepository {
    suspend fun fetchUserInfo(isRefresh: Boolean): Result<User>

    suspend fun checkNicknameDuplication(nickname: Nickname): Result<Boolean>
}
