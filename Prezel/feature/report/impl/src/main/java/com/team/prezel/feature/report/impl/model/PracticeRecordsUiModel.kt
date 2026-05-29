package com.team.prezel.feature.report.impl.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.PracticeRecords
import com.team.prezel.core.ui.component.PracticeCardItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@Immutable
internal data class PracticeRecordsUiModel(
    private val practicedDates: List<LocalDate>,
    val startDate: LocalDate,
    val endDate: LocalDate,
) {
    val practices: ImmutableList<PracticeCardItem> = buildList {
        val practicedDateSet = practicedDates.toSet()
        var currentDate = startDate

        while (currentDate <= endDate) {
            add(
                PracticeCardItem(
                    date = currentDate,
                    isPracticed = currentDate in practicedDateSet,
                ),
            )

            currentDate = currentDate.plus(DatePeriod(days = 1))
        }
    }.toImmutableList()

    companion object {
        fun PracticeRecords.toUiModel(): PracticeRecordsUiModel =
            PracticeRecordsUiModel(
                practicedDates = dates,
                startDate = startDate,
                endDate = endDate,
            )
    }
}
