package com.team.prezel.core.domain.session

interface AuthSessionEventPublisher {
    fun notifySessionExpired()
}
