package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.badge.BadgeEventResponse
import com.team.prezel.core.network.model.badge.GetBadgeDetailResponse
import com.team.prezel.core.network.model.badge.GetBadgeResponse
import kotlinx.coroutines.flow.Flow

interface BadgeRemoteDataSource {
    suspend fun getBadges(sort: String): List<GetBadgeResponse>

    suspend fun getBadgeDetail(badgeCode: String): GetBadgeDetailResponse

    fun connectBadgeEventStream(): Flow<BadgeEventResponse>
}
