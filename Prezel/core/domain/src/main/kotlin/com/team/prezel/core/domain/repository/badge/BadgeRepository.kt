package com.team.prezel.core.domain.repository.badge

import com.team.prezel.core.model.badge.Badge
import com.team.prezel.core.model.badge.BadgeDetail
import com.team.prezel.core.model.badge.BadgeEvent
import kotlinx.coroutines.flow.Flow

interface BadgeRepository {
    suspend fun getBadges(): Result<List<Badge>>

    suspend fun getBadgeDetail(badgeCode: String): Result<BadgeDetail>

    fun connectBadgeEventStream(): Flow<BadgeEvent>
}
