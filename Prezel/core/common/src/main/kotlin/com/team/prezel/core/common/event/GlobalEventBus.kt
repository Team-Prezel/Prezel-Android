package com.team.prezel.core.common.event

import kotlinx.coroutines.flow.Flow

/**
 * feature와 app 계층 사이에서 단발성 전역 이벤트를 전달하는 버스입니다.
 *
 * feature 모듈은 app 모듈을 직접 참조하지 않고 이벤트만 발행하고,
 * app 모듈은 [events]를 수집해 실제 navigation, system bar 변경 같은 앱 단위 동작을 수행합니다.
 */
interface GlobalEventBus {
    /**
     * 앱 전역 이벤트 스트림입니다.
     */
    val events: Flow<GlobalEvent>

    /**
     * suspend context에서 전역 이벤트를 발행합니다.
     */
    suspend fun emit(event: GlobalEvent)

    /**
     * suspend할 수 없는 context에서 전역 이벤트 발행을 시도합니다.
     *
     * Compose dispose callback처럼 suspend 함수를 호출할 수 없는 곳에서 복원 이벤트를 발행할 때 사용합니다.
     */
    fun tryEmit(event: GlobalEvent): Boolean
}
