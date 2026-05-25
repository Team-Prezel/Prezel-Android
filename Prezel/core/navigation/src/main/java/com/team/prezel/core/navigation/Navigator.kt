package com.team.prezel.core.navigation

import androidx.navigation3.runtime.NavKey

/**
 * 내비게이션 이벤트를 처리하며 [NavigationState]를 변경합니다.
 *
 * 정책 요약:
 * - 최상위 키(top-level)는 [topLevelStack][NavigationState.topLevelStack]에 "현재 탭 1개"만 유지합니다.
 * - 각 최상위 키의 상세 이동은 [subStacks][NavigationState.subStacks]에서 관리합니다.
 * - 동일 키 재진입 시에는 스택 중복을 만들지 않고 기존 항목을 재정렬하거나 하위 스택을 초기화합니다.
 */
class Navigator(
    private val state: NavigationState,
) {
    /**
     * 목적지로 이동합니다.
     *
     * 정책:
     * - 동일한 최상위 키를 다시 선택한 경우 → 하위 스택을 초기화합니다.
     * - 최상위 키인 경우 → 탭을 전환합니다.
     * - 그 외의 경우 → 현재 하위 스택에서 동일 키를 제거 후 마지막에 추가합니다.
     *
     * @param key 이동할 목적지 키
     */
    fun navigate(
        key: NavKey,
        clearStack: Boolean = false,
    ) {
        when (key) {
            state.currentTopLevelKey -> clearSubStack()
            in state.topLevelKeys -> goToTopLevel(key)
            else -> goToKey(key = key, clearStack = clearStack)
        }
    }

    /**
     * 현재 내비게이션 히스토리를 모두 지우고 목적지를 루트로 교체합니다.
     *
     * 정책:
     * - 최상위 키만 허용합니다.
     * - [topLevelStack][NavigationState.topLevelStack]은 전달된 키 1개만 남깁니다.
     * - 모든 하위 스택은 루트(인덱스 0)만 남기고 초기화합니다.
     *
     * @param key 새로운 루트가 될 최상위 키
     * @throws IllegalArgumentException [key]가 최상위 키가 아니면 발생합니다.
     */
    fun replaceRoot(key: NavKey) {
        require(key in state.topLevelKeys) {
            "replaceRoot() only supports top-level keys: $key"
        }
        state.topLevelStack.clear()
        state.topLevelStack.add(key)

        state.subStacks.values.forEach { stack ->
            if (stack.size > 1) stack.subList(1, stack.size).clear()
        }
    }

    /**
     * 뒤로 이동을 처리합니다.
     *
     * 처리 순서:
     * 1) 현재 하위 스택에 상세 목적지가 있으면 하위 스택에서 pop 합니다.
     * 2) 아니고 최상위 스택 히스토리가 있으면 최상위 스택에서 pop 합니다. (레거시 상태 호환)
     * 3) 둘 다 불가능하면 아무 동작도 하지 않습니다.
     *
     * @return 뒤로가기 내비게이션이 처리되었으면 true를 반환합니다.
     */
    fun goBack(): Boolean =
        when {
            state.currentKey != state.currentTopLevelKey -> {
                state.currentSubStack.removeLastOrNull()
                true
            }

            state.topLevelStack.size > 1 -> {
                state.topLevelStack.removeLastOrNull()
                true
            }

            else -> false
        }

    /**
     * 최상위가 아닌 목적지로 이동합니다.
     *
     * 정책:
     * - [clearStack]가 true면 현재 하위 스택의 루트만 남기고 제거합니다.
     * - 동일 키가 이미 존재하면 제거 후 마지막에 추가합니다.
     * - 결과적으로 하위 스택 내 동일 키는 1개만 유지됩니다.
     */
    private fun goToKey(
        key: NavKey,
        clearStack: Boolean,
    ) {
        state.currentSubStack.apply {
            if (clearStack && size > 1) {
                subList(1, size).clear()
            }

            remove(key)
            add(key)
        }
    }

    /**
     * 최상위 목적지로 전환합니다.
     *
     * 정책:
     * - 최상위 스택은 항상 현재 키 1개만 유지합니다.
     * - 탭 간 이동 히스토리를 보관하지 않아, 최상위 루트 화면에서 뒤로가기 시
     *   "이전 탭으로 복귀" 대신 시스템 back(앱 종료/상위 핸들러)로 이어지도록 합니다.
     */
    private fun goToTopLevel(key: NavKey) {
        state.topLevelStack.apply {
            clear()
            add(key)
        }
    }

    /**
     * 현재 최상위 키의 하위 스택을 초기화합니다.
     *
     * 정책:
     * - 루트(인덱스 0)는 유지합니다.
     * - 루트를 제외한 상세 히스토리만 제거합니다.
     */
    private fun clearSubStack() {
        state.currentSubStack.run {
            if (size > 1) subList(1, size).clear()
        }
    }
}
