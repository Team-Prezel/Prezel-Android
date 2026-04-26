package com.team.prezel.core.domain.session

import kotlinx.coroutines.flow.Flow

interface AuthSessionMonitor {
    val sessionEvents: Flow<AuthSessionEvent>

    fun acknowledgeSessionEvent()
}
