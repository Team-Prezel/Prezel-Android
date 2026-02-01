package com.team.prezel.core.data

import kotlinx.coroutines.flow.Flow

/**
 * 앱의 네트워크 연결 상태를 보고하기 위한 유틸리티
 */
interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}
