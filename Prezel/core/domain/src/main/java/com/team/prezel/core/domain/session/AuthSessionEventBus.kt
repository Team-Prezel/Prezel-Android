package com.team.prezel.core.domain.session

import kotlinx.coroutines.flow.SharedFlow

interface AuthSessionEventStream {
    val events: SharedFlow<AuthSessionEvent>
}

interface AuthSessionEventPublisher {
    fun notifySessionExpired()
}
