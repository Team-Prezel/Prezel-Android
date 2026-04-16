package com.team.prezel.core.data.repository

import com.team.prezel.core.domain.repository.profile.UserRepository
import com.team.prezel.core.model.profile.Nickname
import kotlinx.coroutines.delay
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor() : UserRepository {
    override suspend fun checkNicknameDuplication(nickname: Nickname): Result<Boolean> {
        // TODO: 실제 API 연동 후 서버 중복 검사 결과를 반환하도록 교체
        delay(200)
        return Result.success(false)
    }
}
