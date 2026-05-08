package com.team.prezel.core.domain.usecase.user

import com.team.prezel.core.model.badge.Badge
import com.team.prezel.core.model.badge.BadgeType
import javax.inject.Inject
import kotlin.random.Random

class FetchUserBadgesUseCase @Inject constructor() {
    suspend operator fun invoke(): Result<List<Badge>> =
        runCatching {
            listOf(
                BadgeType.FIRST_PRESENTATION,
                BadgeType.SECOND_ANALYSIS,
                BadgeType.FIRST_PRACTICE,
                BadgeType.RETROSPECT_COMPLETED,
                BadgeType.PERFECT_SCORE,
                BadgeType.TEN_ANALYSIS,
            ).map { type -> Badge(type = type, isAchieved = Random.nextBoolean()) }
        }
}
