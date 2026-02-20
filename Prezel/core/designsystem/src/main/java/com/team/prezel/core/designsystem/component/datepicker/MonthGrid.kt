package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun MonthGrid(
    month: YearMonth,
    selectedDate: LocalDate?,
    today: LocalDate,
    onSelect: (LocalDate) -> Unit,
) {
    val (cells, lastWeek) = remember(month) {
        val c = buildMonthGrid(month, firstDayOfWeek = DayOfWeek.SUNDAY)
        c to lastWeekIndexToRender(c)
    }

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        for (week in 0..lastWeek) {
            Row(Modifier.fillMaxWidth()) {
                for (day in 0 until 7) {
                    val cell = cells[week * 7 + day]
                    val uiModel = cell.toUiModel(selectedDate = selectedDate, today = today)

                    DayCellView(
                        uiModel = uiModel,
                        onClick = { cell.date?.let(onSelect) },
                    )
                }
            }
        }
    }
}
