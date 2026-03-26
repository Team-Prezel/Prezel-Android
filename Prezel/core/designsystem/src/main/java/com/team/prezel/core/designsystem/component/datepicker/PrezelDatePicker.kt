package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.component.PrezelDividerType
import com.team.prezel.core.designsystem.component.PrezelHorizontalDivider
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewDefaults
import com.team.prezel.core.designsystem.preview.PreviewSurface
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun PrezelDatePicker(
    title: String,
    selectedDate: LocalDate?,
    onSelect: (LocalDate) -> Unit,
    onClose: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
) {
    val initialMonth = remember(today) { YearMonth(today.year, today.month) }
    val months = remember(initialMonth) {
        List(12) { offset ->
            initialMonth.plus(value = offset, unit = DateTimeUnit.MONTH)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        DatePickerHeader(title = title, onClose = onClose)

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = PrezelTheme.spacing.V16),
            overscrollEffect = null,
        ) {
            items(items = months, key = { it.toString() }) { month ->
                MonthSection(
                    yearMonth = month,
                    selectedDate = selectedDate,
                    today = today,
                    onSelect = onSelect,
                )
            }
        }

        DatePickerFooter(selectedDate = selectedDate, onConfirm = onConfirm)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerHeader(
    title: String,
    onClose: () -> Unit,
) {
    Column {
        PrezelTopAppBar(
            title = { Text(text = title) },
            trailingIcons = {
                IconButton(onClick = onClose) {
                    Icon(
                        painter = painterResource(PrezelIcons.Cancel),
                        contentDescription = stringResource(R.string.core_designsystem_close_date_picker_desc),
                    )
                }
            },
        )
        WeekdayRow()
        PrezelHorizontalDivider(type = PrezelDividerType.THICK)
    }
}

@Composable
private fun DatePickerFooter(
    selectedDate: LocalDate?,
    onConfirm: (LocalDate) -> Unit,
) {
    val buttonLabel = stringResource(R.string.core_designsystem_date_picker_confirm_btn)

    PrezelButtonArea(
        isVertical = false,
        showBackground = true,
    ) {
        MainButton(
            label = buttonLabel,
            enabled = selectedDate != null,
            onClick = { selectedDate?.let(onConfirm) },
        )
    }
}

@Composable
private fun WeekdayRow() {
    val labels = stringArrayResource(R.array.core_designsystem_weekday_labels).toList()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = PrezelTheme.spacing.V20,
                vertical = PrezelTheme.spacing.V12,
            ),
    ) {
        labels.forEach { text ->
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = text,
                    color = PrezelTheme.colors.textMedium,
                    style = PrezelTheme.typography.body3Medium,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrezelDatePickerPreview() {
    var selected by remember {
        mutableStateOf(LocalDate(year = 2026, month = 2, day = 26))
    }

    PreviewSurface(
        defaults = PreviewDefaults(screenPadding = PaddingValues(0.dp)),
    ) {
        PrezelDatePicker(
            title = "발표 날짜",
            today = LocalDate(year = 2026, month = 2, day = 23),
            selectedDate = selected,
            onSelect = { selected = it },
            onClose = {},
            onConfirm = {},
        )
    }
}
