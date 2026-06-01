package com.team.prezel.core.domain.usecase.badge

import com.team.prezel.core.domain.repository.badge.BadgeRepository
import com.team.prezel.core.model.badge.BadgeDetail
import javax.inject.Inject

class FetchBadgeDetailUseCase @Inject constructor(
    private val badgeRepository: BadgeRepository,
) {
    suspend operator fun invoke(badgeCode: String): Result<BadgeDetail> = badgeRepository.getBadgeDetail(badgeCode = badgeCode)
}
