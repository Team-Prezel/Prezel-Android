package com.team.prezel.core.domain.repository.profile

import com.team.prezel.core.model.profile.Nickname

interface UserRepository {
    suspend fun checkNicknameDuplication(nickname: Nickname): Result<Boolean>
}
