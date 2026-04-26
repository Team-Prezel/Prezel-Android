package com.team.prezel.core.domain.session

sealed interface AuthSessionEvent {
    data object Expired : AuthSessionEvent
}
