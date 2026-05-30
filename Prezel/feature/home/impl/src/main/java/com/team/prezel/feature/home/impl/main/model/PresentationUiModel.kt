package com.team.prezel.feature.home.impl.main.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.MainDataWithPracticeRecords
import com.team.prezel.core.ui.component.PracticeCardItem
import com.team.prezel.feature.home.impl.main.model.GrowthGraphData.Companion.toUiModel
import com.team.prezel.feature.home.impl.main.model.PracticeRecordsUiModel.Companion.toUiModel
import kotlinx.datetime.LocalDate

@Immutable
internal sealed interface PresentationUiModel {
    val id: Long
    val category: Category
    val title: String
    val date: LocalDate
    val dDay: String
    val practiceRecords: PracticeRecordsUiModel
    val practiceCount: Int
        get() = practiceRecords.practices.count(PracticeCardItem::isPracticed)
    val isPastPresentation: Boolean
        get() = this is Past

    data class Past(
        override val id: Long,
        override val category: Category,
        override val title: String,
        override val date: LocalDate,
        override val dDay: String,
        override val practiceRecords: PracticeRecordsUiModel,
        val growthGraphData: GrowthGraphData,
    ) : PresentationUiModel

    data class Upcoming(
        override val id: Long,
        override val category: Category,
        override val title: String,
        override val date: LocalDate,
        override val dDay: String,
        override val practiceRecords: PracticeRecordsUiModel,
    ) : PresentationUiModel

    companion object {
        fun MainDataWithPracticeRecords.toUiModel(): PresentationUiModel =
            if (isPast) {
                Past(
                    id = presentationId,
                    category = Category.from(type),
                    title = title,
                    date = presentationDate,
                    dDay = dDay,
                    practiceRecords = practiceRecords.toUiModel(),
                    growthGraphData = growthGraph.toUiModel(),
                )
            } else {
                Upcoming(
                    id = presentationId,
                    category = Category.from(type),
                    title = title,
                    date = presentationDate,
                    dDay = dDay,
                    practiceRecords = practiceRecords.toUiModel(),
                )
            }
    }
}
