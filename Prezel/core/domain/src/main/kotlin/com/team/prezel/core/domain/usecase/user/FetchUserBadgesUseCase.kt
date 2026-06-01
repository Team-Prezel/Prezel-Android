package com.team.prezel.core.domain.usecase.user

import com.team.prezel.core.domain.usecase.badge.FetchBadgesUseCase
import com.team.prezel.core.model.badge.Badge
import javax.inject.Inject

class FetchUserBadgesUseCase @Inject constructor(
    private val fetchBadgesUseCase: FetchBadgesUseCase,
) {
    suspend operator fun invoke(): Result<List<Badge>> = fetchBadgesUseCase()
}
