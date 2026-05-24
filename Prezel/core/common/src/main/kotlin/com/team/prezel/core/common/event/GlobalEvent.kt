package com.team.prezel.core.common.event

/**
 * 앱 전역에서 단발성으로 처리해야 하는 이벤트입니다.
 *
 * 화면 전환, 인증 만료 처리, 시스템 바 스타일 변경처럼 특정 feature가 직접 app 계층을 참조하지 않고
 * 루트 화면에서 일괄 처리해야 하는 동작을 전달할 때 사용합니다.
 */
sealed interface GlobalEvent {
    /**
     * 인증 상태가 만료되어 앱을 splash/root 흐름으로 되돌려야 하는 이벤트입니다.
     */
    data object ForceLogout : GlobalEvent

    /**
     * edge-to-edge 상태바 영역의 배경 스타일을 변경합니다.
     *
     * 실제 Window 및 상태바 overlay 적용은 app 계층에서 처리합니다.
     */
    data class ChangeEdgeToEdgeStatusBarStyle(
        val style: EdgeToEdgeStatusBarStyle,
    ) : GlobalEvent

    /**
     * feature 화면에서 변경한 edge-to-edge 상태바 스타일을 앱 기본 상태로 되돌립니다.
     */
    data object ResetEdgeToEdgeStatusBarStyle : GlobalEvent
}

/**
 * edge-to-edge 상태바 영역에 적용할 앱 공통 배경 스타일입니다.
 */
enum class EdgeToEdgeStatusBarStyle {
    /**
     * 앱 기본 edge-to-edge 상태로 복원합니다.
     */
    DEFAULT,

    /**
     * 일반 배경색을 상태바 영역에 적용합니다.
     */
    BG_REGULAR,

    /**
     * 강조/헤더 배경색을 상태바 영역에 적용합니다.
     */
    BG_MEDIUM,
}
