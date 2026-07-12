package com.team.prezel.core.data.repository

import com.team.prezel.core.data.error.mapDomainFailure
import com.team.prezel.core.domain.repository.badge.BadgeRepository
import com.team.prezel.core.model.badge.Badge
import com.team.prezel.core.model.badge.BadgeDetail
import com.team.prezel.core.model.badge.BadgeEvent
import com.team.prezel.core.model.badge.BadgeSortType
import com.team.prezel.core.network.datasource.BadgeRemoteDataSource
import com.team.prezel.core.network.model.badge.BadgeEventResponse
import com.team.prezel.core.network.model.badge.GetBadgeDetailResponse
import com.team.prezel.core.network.model.badge.GetBadgeResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class BadgeRepositoryImpl @Inject constructor(
    private val badgeRemoteDataSource: BadgeRemoteDataSource,
) : BadgeRepository {
    override suspend fun getBadges(sort: BadgeSortType): Result<List<Badge>> =
        runCatching {
            badgeRemoteDataSource.getBadges(sort.value.lowercase())
        }.mapCatching { response ->
            response.map(GetBadgeResponse::toDomain)
        }.mapDomainFailure()

    override suspend fun getBadgeDetail(badgeCode: String): Result<BadgeDetail> =
        runCatching {
            badgeRemoteDataSource.getBadgeDetail(badgeCode = badgeCode)
        }.mapCatching(GetBadgeDetailResponse::toDomain)
            .mapDomainFailure()

    override fun connectBadgeEventStream(): Flow<BadgeEvent> =
        badgeRemoteDataSource
            .connectBadgeEventStream()
            .map(BadgeEventResponse::toDomain)
}

private fun GetBadgeResponse.toDomain(): Badge =
    Badge(
        badgeCode = badgeCode,
        badgeName = badgeName,
        isUnlocked = isUnlocked,
        imageUrl = imageUrl,
        unlockedAt = unlockedAt,
    )

private fun GetBadgeDetailResponse.toDomain(): BadgeDetail =
    BadgeDetail(
        badgeCode = badgeCode,
        badgeName = badgeName,
        conditionText = conditionText,
        detailDescription = detailDescription,
        imageUrl = imageUrl,
        isUnlocked = isUnlocked,
        unlockedAt = unlockedAt,
    )

private fun BadgeEventResponse.toDomain(): BadgeEvent =
    BadgeEvent(
        badgeCode = badgeCode,
        badgeName = badgeName,
        introduction = introduction,
        imageUrl = imageUrl,
        message = message,
        rawData = rawData,
    )
