package com.team.prezel.core.designsystem.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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

@Immutable
internal data class PreviewMatrixColumn(
    val header: String,
    val weight: Float = 1f,
)

@Immutable
internal data class PreviewMatrixRow<T>(
    val label: String,
    val values: List<T>,
    val labelWeight: Float = 1f,
)

@Immutable
internal data class PreviewMatrixDefaults(
    val titleSpacing: Dp = 12.dp,
    val headerHorizontalPadding: Dp = 8.dp,
    val headerVerticalPadding: Dp = 10.dp,
    val cellHorizontalPadding: Dp = 8.dp,
    val cellVerticalPadding: Dp = 14.dp,
)

/**
 * 디자인시스템 Preview의 기본 배경, 패딩, 테마를 제공합니다.
 *
 * 대부분의 Preview는 이 컴포넌트를 기본 루트로 사용합니다.
 * 화면 전체 문맥이 필요한 경우에만 `PreviewScaffold`를 사용합니다.
 */
@Composable
internal fun PreviewSurface(
    modifier: Modifier = Modifier,
    defaults: PreviewDefaults = PreviewDefaults(),
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    PrezelTheme {
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = PrezelTheme.colors.bgRegular,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrezelTheme.colors.bgRegular)
                    .padding(defaults.screenPadding),
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
    scrollable: Boolean = false,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(defaults.itemSpacing),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    PrezelTheme {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .then(
                    if (scrollable) {
                        Modifier.verticalScroll(rememberScrollState())
                    } else {
                        Modifier
                    },
                ),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content,
        )
    }
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
    PrezelTheme {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(defaults.itemSpacing),
            verticalAlignment = verticalAlignment,
            content = content,
        )
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
    PreviewSurface(
        modifier = modifier.fillMaxWidth(),
        defaults = defaults,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(defaults.itemSpacing)) {
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

            HorizontalDivider(color = PrezelTheme.colors.borderRegular)

            PreviewColumn(
                defaults = defaults,
                content = content,
            )
        }
    }
}

/**
 * Preview에서 토큰처럼 이름, 값, 샘플 UI를 한 줄에 보여줄 때 사용하는 행 레이아웃입니다.
 */
@Composable
internal fun PreviewValueRow(
    name: String,
    modifier: Modifier = Modifier,
    defaults: PreviewDefaults = PreviewDefaults(),
    valueLabel: String? = null,
    preview: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(defaults.itemSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = PrezelTheme.typography.body3Medium,
                color = PrezelTheme.colors.textMedium,
                modifier = Modifier.weight(1f),
            )
            valueLabel?.let { label ->
                Text(
                    text = label,
                    style = PrezelTheme.typography.body3Regular,
                    color = PrezelTheme.colors.textSmall,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        Box(modifier = Modifier, contentAlignment = Alignment.CenterEnd) {
            preview()
        }
    }
}

@Composable
internal fun <T> PreviewMatrix(
    title: String,
    columns: List<PreviewMatrixColumn>,
    rows: List<PreviewMatrixRow<T>>,
    modifier: Modifier = Modifier,
    leadingHeaderText: String = "Case",
    leadingColumnWeight: Float = 1f,
    defaults: PreviewMatrixDefaults = PreviewMatrixDefaults(),
    headerCellContent: (@Composable (PreviewMatrixColumn) -> Unit)? = null,
    rowLabelContent: (@Composable (PreviewMatrixRow<T>) -> Unit)? = null,
    cellContent: @Composable (T) -> Unit,
) {
    rows.forEach { row ->
        require(row.values.size == columns.size) {
            "PreviewMatrix row '${row.label}' has ${row.values.size} values, expected ${columns.size}."
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(defaults.titleSpacing),
    ) {
        Text(
            text = title,
            style = PrezelTheme.typography.body3Bold,
            color = PrezelTheme.colors.textLarge,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            PreviewMatrixHeaderRow(
                leadingHeaderText = leadingHeaderText,
                leadingColumnWeight = leadingColumnWeight,
                columns = columns,
                defaults = defaults,
                headerCellContent = headerCellContent,
            )

            rows.forEach { row ->
                PreviewMatrixRow(
                    row = row,
                    columns = columns,
                    defaults = defaults,
                    rowLabelContent = rowLabelContent,
                    cellContent = cellContent,
                )
            }
        }
    }
}

@Composable
private fun PreviewMatrixHeaderRow(
    leadingHeaderText: String,
    leadingColumnWeight: Float,
    columns: List<PreviewMatrixColumn>,
    defaults: PreviewMatrixDefaults,
    headerCellContent: (@Composable (PreviewMatrixColumn) -> Unit)?,
) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        PreviewMatrixHeaderCell(
            modifier = Modifier.weight(leadingColumnWeight),
            defaults = defaults,
        ) {
            Text(
                text = leadingHeaderText,
                style = PrezelTheme.typography.caption2Regular,
                color = PrezelTheme.colors.textMedium,
                textAlign = TextAlign.Center,
            )
        }

        columns.forEach { column ->
            PreviewMatrixHeaderCell(
                modifier = Modifier.weight(column.weight),
                defaults = defaults,
            ) {
                if (headerCellContent == null) {
                    Text(
                        text = column.header,
                        style = PrezelTheme.typography.caption2Regular,
                        color = PrezelTheme.colors.textMedium,
                        textAlign = TextAlign.Center,
                    )
                } else {
                    headerCellContent(column)
                }
            }
        }
    }
}

@Composable
private fun <T> PreviewMatrixRow(
    row: PreviewMatrixRow<T>,
    columns: List<PreviewMatrixColumn>,
    defaults: PreviewMatrixDefaults,
    rowLabelContent: (@Composable (PreviewMatrixRow<T>) -> Unit)?,
    cellContent: @Composable (T) -> Unit,
) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        PreviewMatrixCell(
            modifier = Modifier.weight(row.labelWeight),
            defaults = defaults,
        ) {
            if (rowLabelContent == null) {
                Text(
                    text = row.label,
                    style = PrezelTheme.typography.body3Medium,
                    color = PrezelTheme.colors.textLarge,
                    textAlign = TextAlign.Center,
                )
            } else {
                rowLabelContent(row)
            }
        }

        row.values.forEachIndexed { index, value ->
            PreviewMatrixCell(
                modifier = Modifier.weight(columns[index].weight),
                defaults = defaults,
            ) {
                cellContent(value)
            }
        }
    }
}

@Composable
private fun PreviewMatrixHeaderCell(
    modifier: Modifier = Modifier,
    defaults: PreviewMatrixDefaults,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .border(width = PrezelTheme.stroke.V1, color = PrezelTheme.colors.borderRegular)
            .background(PrezelTheme.colors.bgMedium)
            .padding(
                horizontal = defaults.headerHorizontalPadding,
                vertical = defaults.headerVerticalPadding,
            ),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
private fun PreviewMatrixCell(
    modifier: Modifier = Modifier,
    defaults: PreviewMatrixDefaults,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .border(width = PrezelTheme.stroke.V1, color = PrezelTheme.colors.borderRegular)
            .padding(
                horizontal = defaults.cellHorizontalPadding,
                vertical = defaults.cellVerticalPadding,
            ),
        contentAlignment = Alignment.Center,
    ) {
        content()
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
