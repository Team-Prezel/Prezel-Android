package com.team.prezel.core.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events by mutating NavigationState.
 */
class Navigator(
    private val state: NavigationState,
) {
    /**
     * Navigate to a destination.
     *
     * Policy:
     * - Same top-level key reselected → clear sub stack
     * - Top-level key → switch tab
     * - Otherwise → push to current sub stack
     */
    fun navigate(key: NavKey) {
        when (key) {
            state.currentTopLevelKey -> clearSubStack()
            in state.topLevelKeys -> goToTopLevel(key)
            else -> goToKey(key)
        }
    }

    /**
     * Go back.
     *
     * @return true if back navigation was handled
     */
    fun goBack(): Boolean =
        when (state.currentKey) {
            state.startKey -> false
            state.currentTopLevelKey -> {
                state.topLevelStack.removeLastOrNull()
                true
            }

            else -> {
                state.currentSubStack.removeLastOrNull()
                true
            }
        }

    /** Push non-top-level destination */
    private fun goToKey(key: NavKey) {
        state.currentSubStack.apply {
            remove(key)
            add(key)
        }
    }

    /** Switch top-level destination */
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

    /** Clear current sub stack except root */
    private fun clearSubStack() {
        state.currentSubStack.run {
            if (size > 1) subList(1, size).clear()
        }
    }
}
