package com.team.prezel.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelTextButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.designsystem.util.drawDashBorder
import com.team.prezel.core.ui.util.noRippleClickable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.plus

private const val TRACKER_SIZE = 7
private val StampFormatter: DateTimeFormat<LocalDate> = LocalDate.Format { day() }

private enum class StampType {
    AFTER,
    BEFORE,
    EMPTY,
}

@Immutable
private data class TrackerItem(
    val date: LocalDate,
    val isPracticed: Boolean,
    val type: StampType,
)

@Immutable
data class PracticeCardItem(
    val date: LocalDate,
    val isPracticed: Boolean,
)

@Composable
fun PracticeCard(
    dDay: LocalDate,
    items: ImmutableList<PracticeCardItem>,
    modifier: Modifier = Modifier,
    showActionButton: Boolean = true,
    onClickAction: () -> Unit = {},
) {
    var startIndex by rememberSaveable(items) { mutableIntStateOf(0) }
    val trackerItems = items.toTrackerItems(dDay = dDay)
    val hasPreviousPage = startIndex > 0
    val hasNextPage = startIndex + TRACKER_SIZE < trackerItems.size

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(PrezelTheme.shapes.V6)
            .background(color = PrezelTheme.colors.bgMedium)
            .padding(
                vertical = PrezelTheme.spacing.V12,
                horizontal = PrezelTheme.spacing.V16,
            ),
    ) {
        PracticeCardHeader(
            practicedCount = items.count { item -> item.isPracticed },
            totalCount = items.size,
            hasNextPage = hasNextPage,
            hasPreviousPage = hasPreviousPage,
            onClickLeft = { if (hasPreviousPage) startIndex -= TRACKER_SIZE },
            onClickRight = { if (hasNextPage) startIndex += TRACKER_SIZE },
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))

        PracticeTracker(
            items = trackerItems,
            startIndex = startIndex,
        )

        if (showActionButton) {
            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))

            PrezelTextButton(
                text = "연습하기",
                type = ButtonType.FILLED,
                size = ButtonSize.SMALL,
                hierarchy = ButtonHierarchy.SECONDARY,
                onClick = onClickAction,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun PracticeCardHeader(
    practicedCount: Int,
    totalCount: Int,
    hasNextPage: Boolean,
    hasPreviousPage: Boolean,
    onClickLeft: () -> Unit,
    onClickRight: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PaginationRow(
            showChevron = totalCount > TRACKER_SIZE,
            hasNextPage = hasNextPage,
            hasPreviousPage = hasPreviousPage,
            onClickLeft = onClickLeft,
            onClickRight = onClickRight,
        )

        CountRow(
            practicedCount = practicedCount,
            totalCount = totalCount,
        )
    }
}

@Composable
private fun PaginationRow(
    showChevron: Boolean,
    hasNextPage: Boolean,
    hasPreviousPage: Boolean,
    onClickLeft: () -> Unit,
    onClickRight: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showChevron) {
            Icon(
                painter = painterResource(PrezelIcons.ChevronLeft),
                contentDescription = "이전 페이지",
                tint = chevronIconTintColor(enabled = hasPreviousPage),
                modifier = Modifier.noRippleClickable(onClickLeft),
            )
        }

        Text(
            text = "연습한 횟수",
            style = PrezelTheme.typography.body3Medium,
            color = PrezelTheme.colors.textMedium,
        )

        if (showChevron) {
            Icon(
                painter = painterResource(PrezelIcons.ChevronRight),
                contentDescription = "다음 페이지",
                tint = chevronIconTintColor(enabled = hasNextPage),
                modifier = Modifier.noRippleClickable(onClickRight),
            )
        }
    }
}

@Composable
private fun chevronIconTintColor(enabled: Boolean): Color = if (enabled) PrezelTheme.colors.iconRegular else PrezelTheme.colors.iconDisabled

@Composable
private fun CountRow(
    practicedCount: Int,
    totalCount: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = PrezelTheme.colors.textMedium)) {
                append(practicedCount.toString())
            }
            withStyle(style = SpanStyle(color = PrezelTheme.colors.textSmall)) {
                append("/")
                append(totalCount.toString())
            }
        },
        style = PrezelTheme.typography.body3Medium,
        modifier = modifier,
    )
}

@Composable
private fun PracticeTracker(
    items: ImmutableList<TrackerItem>,
    startIndex: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        items
            .drop(startIndex)
            .take(TRACKER_SIZE)
            .forEach { item ->
                PracticeStamp(date = item.date, type = item.type)
            }
    }
}

private fun ImmutableList<PracticeCardItem>.toTrackerItems(dDay: LocalDate): ImmutableList<TrackerItem> {
    val blankCount = (TRACKER_SIZE - (size % TRACKER_SIZE))
        .takeIf { count -> count != TRACKER_SIZE } ?: 0

    val lastDate = lastOrNull()?.date ?: dDay

    val blankItems = List(blankCount) { index ->
        PracticeCardItem(
            date = lastDate.plus(DatePeriod(days = index + 1)),
            isPracticed = false,
        )
    }

    return (this + blankItems)
        .map { item ->
            TrackerItem(
                date = item.date,
                isPracticed = item.isPracticed,
                type = when {
                    item.date >= dDay -> StampType.EMPTY
                    item.isPracticed -> StampType.AFTER
                    else -> StampType.BEFORE
                },
            )
        }.toImmutableList()
}

@Composable
private fun PracticeStamp(
    date: LocalDate,
    type: StampType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = date.format(StampFormatter),
            style = PrezelTheme.typography.caption2Regular,
            color = type.textColor(),
        )
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V2))

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(PrezelTheme.shapes.V1000)
                .background(color = type.bgColor())
                .applyDashBorder(type = type),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(PrezelIcons.Check),
                contentDescription = "",
                tint = type.tintColor(),
            )
        }
    }
}

@Composable
private fun StampType.textColor(): Color =
    when (this) {
        StampType.EMPTY -> PrezelTheme.colors.textDisabled
        StampType.AFTER,
        StampType.BEFORE,
        -> PrezelTheme.colors.textRegular
    }

@Composable
private fun StampType.bgColor(): Color =
    when (this) {
        StampType.AFTER -> PrezelTheme.colors.interactiveRegular
        StampType.BEFORE -> PrezelTheme.colors.bgLarge
        StampType.EMPTY -> PrezelTheme.colors.bgDisabled
    }

@Composable
private fun StampType.tintColor(): Color =
    when (this) {
        StampType.AFTER -> PrezelTheme.colors.solidWhite
        StampType.BEFORE -> PrezelTheme.colors.iconDisabled
        StampType.EMPTY -> Color.Transparent
    }

@Composable
private fun Modifier.applyDashBorder(type: StampType): Modifier {
    if (type != StampType.BEFORE) return this
    val density = LocalDensity.current

    return drawDashBorder(
        shape = PrezelTheme.shapes.V1000,
        color = PrezelTheme.colors.borderMedium,
        width = with(density) { 1.dp.toPx() },
        interval = with(density) { 2.dp.toPx() },
    )
}

@BasicPreview
@Composable
private fun PracticeCardPreview() {
    val baseDate = LocalDate(year = 2026, month = 3, day = 20)
    val dDay = LocalDate(year = 2026, month = 3, day = 30)

    PrezelTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            PracticeCard(
                dDay = dDay,
                items = List(baseDate.daysUntil(dDay)) { index ->
                    PracticeCardItem(
                        date = baseDate.plus(DatePeriod(days = index)),
                        isPracticed = index % 2 == 0,
                    )
                }.toPersistentList(),
            )
        }
    }
}
