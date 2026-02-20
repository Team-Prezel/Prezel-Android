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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.component.PrezelDividerType
import com.team.prezel.core.designsystem.component.PrezelHorizontalDivider
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.component.button.PrezelButtonHierarchy
import com.team.prezel.core.designsystem.component.button.PrezelButtonSize
import com.team.prezel.core.designsystem.component.button.PrezelButtonStyle
import com.team.prezel.core.designsystem.component.button.PrezelButtonType
import com.team.prezel.core.designsystem.component.button.PrezelTextButton
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrezelDatePicker(
    title: String,
    initialMonth: YearMonth,
    today: LocalDate,
    selectedDate: LocalDate?,
    onSelect: (LocalDate) -> Unit,
    onClose: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val months = remember(initialMonth) {
        (0..12).map { initialMonth.plusMonths(it.toLong()) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        DatePickerHeader(title = title, onClose = onClose)

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            items(months, key = { it.toString() }) { month ->
                MonthSection(
                    month = month,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerFooter(
    selectedDate: LocalDate?,
    onConfirm: (LocalDate) -> Unit,
) {
    Column {
        PrezelHorizontalDivider(type = PrezelDividerType.THICK)
        PrezelTextButton(
            text = stringResource(R.string.core_designsystem_date_picker_confirm_btn),
            onClick = { selectedDate?.let(onConfirm) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            enabled = selectedDate != null,
            PrezelButtonStyle(
                buttonType = PrezelButtonType.FILLED,
                buttonHierarchy = PrezelButtonHierarchy.PRIMARY,
                buttonSize = PrezelButtonSize.REGULAR,
            ),
        )
    }
}

@Composable
private fun WeekdayRow() {
    val labels = listOf("일", "월", "화", "수", "목", "금", "토")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
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
    PrezelTheme {
        var selected by remember {
            mutableStateOf(LocalDate.of(2026, 2, 26))
        }

        PrezelDatePicker(
            title = "발표 날짜",
            initialMonth = YearMonth.of(2026, 2),
            today = LocalDate.of(2026, 2, 20),
            selectedDate = selected,
            onSelect = { selected = it },
            onClose = {},
            onConfirm = {},
        )
    }
}
