package com.team.prezel.core.designsystem.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.theme.PrezelTheme

/**
 * Preview 공통 레이아웃 설정입니다.
 */
@Immutable
internal data class PreviewDefaults(
    val screenPadding: PaddingValues = PaddingValues(16.dp),
    val sectionSpacing: Dp = 24.dp,
    val itemSpacing: Dp = 12.dp,
)

/**
 * 디자인시스템 Preview의 기본 배경, 패딩, 테마를 제공합니다.
 *
 * 대부분의 Preview는 이 컴포넌트로 감싸는 것을 권장합니다.
 */
@Composable
internal fun PreviewSurface(
    modifier: Modifier = Modifier,
    defaults: PreviewDefaults = PreviewDefaults(),
    useSystemInsets: Boolean = false,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    PrezelTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = PrezelTheme.colors.bgRegular,
        ) {
            val containerModifier = Modifier
                .fillMaxSize()
                .background(PrezelTheme.colors.bgRegular)
                .then(
                    if (useSystemInsets) {
                        Modifier.padding(WindowInsets.safeDrawing.asPaddingValues())
                    } else {
                        Modifier
                    },
                ).padding(defaults.screenPadding)

            Box(
                modifier = containerModifier,
                contentAlignment = contentAlignment,
                content = content,
            )
        }
    }
}

/**
 * Preview에서 여러 상태를 세로로 나열할 때 사용하는 공통 레이아웃입니다.
 */
@Composable
internal fun PreviewColumn(
    modifier: Modifier = Modifier,
    defaults: PreviewDefaults = PreviewDefaults(),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(defaults.itemSpacing),
        horizontalAlignment = horizontalAlignment,
        content = content,
    )
}

/**
 * Preview에서 작은 컴포넌트를 가로로 비교할 때 사용하는 공통 레이아웃입니다.
 */
@Composable
internal fun PreviewRow(
    modifier: Modifier = Modifier,
    defaults: PreviewDefaults = PreviewDefaults(),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(defaults.itemSpacing),
        verticalAlignment = verticalAlignment,
        content = content,
    )
}

/**
 * Preview에서 반복 가능한 작은 컴포넌트를 그리드 형태로 보여줍니다.
 *
 * 칩, 아이콘 버튼, 배지처럼 여러 변형을 한 번에 확인할 때 유용합니다.
 */
@Composable
internal fun <T> PreviewGrid(
    items: List<T>,
    modifier: Modifier = Modifier,
    columns: Int = 2,
    defaults: PreviewDefaults = PreviewDefaults(),
    key: ((T) -> Any)? = null,
    itemContent: @Composable (T) -> Unit,
) {
    require(columns > 0) {
        "columns는 1 이상이어야 합니다."
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(defaults.itemSpacing),
        verticalArrangement = Arrangement.spacedBy(defaults.itemSpacing),
        userScrollEnabled = true,
    ) {
        if (key == null) {
            items(items) { item ->
                itemContent(item)
            }
            return@LazyVerticalGrid
        }

        items(
            items = items,
            key = key,
        ) { item ->
            itemContent(item)
        }
    }
}

/**
 * Preview 내에서 상태 그룹을 제목과 함께 구분해 보여줍니다.
 */
@Composable
internal fun PreviewSection(
    title: String,
    modifier: Modifier = Modifier,
    defaults: PreviewDefaults = PreviewDefaults(),
    description: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(defaults.itemSpacing),
    ) {
        Text(
            text = title,
            style = PrezelTheme.typography.title2Medium,
            color = PrezelTheme.colors.textLarge,
        )

        description?.let { value ->
            Text(
                text = value,
                style = PrezelTheme.typography.body3Regular,
                color = PrezelTheme.colors.textRegular,
            )
        }

        PreviewColumn(
            defaults = defaults,
            content = content,
        )
    }
}

/**
 * TopBar, BottomBar, Snackbar 등 Scaffold 문맥이 필요한 Preview를 위한 래퍼입니다.
 */
@Composable
internal fun PreviewScaffold(
    modifier: Modifier = Modifier,
    defaults: PreviewDefaults = PreviewDefaults(),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    PrezelTheme {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = topBar,
            bottomBar = bottomBar,
            floatingActionButton = floatingActionButton,
            snackbarHost = snackbarHost,
            containerColor = PrezelTheme.colors.bgRegular,
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(defaults.screenPadding),
            ) {
                content(innerPadding)
            }
        }
    }
}
