package com.team.prezel.core.common.event

sealed interface GlobalEvent {
    data object ForceLogout : GlobalEvent
}
