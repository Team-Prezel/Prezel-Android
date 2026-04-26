package com.team.prezel.core.domain.session

import kotlinx.coroutines.flow.Flow

interface AuthSessionEventStream {
    val events: Flow<AuthSessionEvent>

    fun clearSessionExpiredEvent()
}

interface AuthSessionEventPublisher {
    fun notifySessionExpired()
}
