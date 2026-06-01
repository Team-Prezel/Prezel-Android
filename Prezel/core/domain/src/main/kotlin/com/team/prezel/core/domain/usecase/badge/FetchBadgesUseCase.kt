package com.team.prezel.core.domain.usecase.badge

import com.team.prezel.core.domain.repository.badge.BadgeRepository
import com.team.prezel.core.model.badge.Badge
import javax.inject.Inject

class FetchBadgesUseCase @Inject constructor(
    private val badgeRepository: BadgeRepository,
) {
    suspend operator fun invoke(): Result<List<Badge>> = badgeRepository.getBadges()
}
