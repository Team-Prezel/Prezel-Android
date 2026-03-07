package com.team.prezel.core.navigation

import androidx.navigation3.runtime.NavKey

/**
 * 내비게이션 이벤트를 처리하며, NavigationState를 변경합니다.
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
     * - 그 외의 경우 → 현재 하위 스택에 목적지를 push 합니다.
     */
    fun navigate(key: NavKey) {
        when (key) {
            state.currentTopLevelKey -> clearSubStack()
            in state.topLevelKeys -> goToTopLevel(key)
            else -> goToKey(key)
        }
    }

    /**
     * 현재 내비게이션 히스토리를 모두 지우고 목적지를 루트로 교체합니다.
     */
    fun replaceRoot(key: NavKey) {
        state.topLevelStack.clear()
        state.topLevelStack.add(key)
        if (key in state.topLevelKeys) {
            state.subStacks[key]?.run {
                if (size > 1) subList(1, size).clear()
            }
        }
    }

    /**
     * 뒤로 이동
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

    /** 최상위가 아닌 목적지를 push합니다. */
    private fun goToKey(key: NavKey) {
        state.currentSubStack.apply {
            remove(key)
            add(key)
        }
    }

    /** 최상위 목적지로 전환합니다. */
    private fun goToTopLevel(key: NavKey) {
        state.topLevelStack.apply {
            if (key == state.startKey) {
                clear()
            } else {
                remove(key)
            }
            add(key)
        }
    }

    /** 루트를 제외하고 현재 하위 스택을 초기화합니다. */
    private fun clearSubStack() {
        state.currentSubStack.run {
            if (size > 1) subList(1, size).clear()
        }
    }
}
