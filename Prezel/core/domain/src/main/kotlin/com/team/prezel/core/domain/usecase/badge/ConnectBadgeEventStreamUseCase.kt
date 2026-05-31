package com.team.prezel.core.domain.usecase.badge

import com.team.prezel.core.domain.repository.badge.BadgeRepository
import com.team.prezel.core.model.badge.BadgeEvent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConnectBadgeEventStreamUseCase @Inject constructor(
    private val badgeRepository: BadgeRepository,
) {
    operator fun invoke(): Flow<BadgeEvent> = badgeRepository.connectBadgeEventStream()
}
