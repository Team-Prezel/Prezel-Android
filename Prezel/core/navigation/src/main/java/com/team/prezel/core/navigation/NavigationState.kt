package com.team.prezel.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import kotlinx.collections.immutable.ImmutableSet

/**
 * 구성 변경과 프로세스 종료 후 복원에도 살아남는 네비게이션 상태를 생성합니다.
 */
@Composable
fun rememberNavigationState(
    startKey: NavKey,
    topLevelKeys: ImmutableSet<NavKey>,
): NavigationState {
    val topLevelStack = rememberNavBackStack(startKey)
    val subStacks = topLevelKeys.associateWith { key -> rememberNavBackStack(key) }

    return remember(startKey, topLevelKeys) {
        NavigationState(
            startKey = startKey,
            topLevelStack = topLevelStack,
            subStacks = subStacks,
        )
    }
}

/**
 * 내비게이션을 위한 State Holder
 *
 * @param startKey - 시작 내비게이션 키입니다. 사용자는 이 키를 통해 앱을 종료하게 됩니다.
 * @param topLevelStack - 최상위 백 스택입니다. 최상위 키만을 보관합니다.
 * @param subStacks - 각 최상위 키에 대응하는 하위 백 스택들입니다.
 */
class NavigationState(
    val startKey: NavKey,
    val topLevelStack: NavBackStack<NavKey>,
    val subStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    val currentTopLevelKey: NavKey by derivedStateOf {
        topLevelStack.last()
    }

    val topLevelKeys: Set<NavKey>
        get() = subStacks.keys

    val currentSubStack: NavBackStack<NavKey>
        get() = subStacks[currentTopLevelKey]
            ?: error("Sub stack for $currentTopLevelKey does not exist")

    val currentKey: NavKey by derivedStateOf { currentSubStack.last() }
}

/**
 * NavHost에서 사용할 수 있도록 NavigationState를 NavEntries로 변환합니다.
 */
@Composable
fun NavigationState.toEntries(entryProvider: (NavKey) -> NavEntry<NavKey>): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries = subStacks.mapValues { (_, stack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberViewModelStoreNavEntryDecorator<NavKey>(),
        )

        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider,
        )
    }

    return topLevelStack
        .flatMap { key -> decoratedEntries[key].orEmpty() }
        .toMutableStateList()
}
