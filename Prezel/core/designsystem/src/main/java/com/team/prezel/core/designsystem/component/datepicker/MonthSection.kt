package com.team.prezel.core.designsystem.component.datepicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.theme.PrezelTheme
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun MonthSection(
    month: YearMonth,
    selectedDate: LocalDate?,
    today: LocalDate,
    onSelect: (LocalDate) -> Unit,
) {
    Column(
        modifier = Modifier.padding(PrezelTheme.spacing.V20),
    ) {
        Text(
            text = "${month.year}년 ${month.monthValue}월",
            color = PrezelTheme.colors.textLarge,
            style = PrezelTheme.typography.body3Medium,
        )

        MonthGrid(
            month = month,
            selectedDate = selectedDate,
            today = today,
            onSelect = onSelect,
        )
    }
}
