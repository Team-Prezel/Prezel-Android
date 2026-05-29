package com.team.prezel.core.model.presentation

import kotlinx.datetime.LocalDate

data class MainDataWithPracticeRecords(
    val presentationId: Long,
    val title: String,
    val type: String,
    val presentationDate: LocalDate,
    val isPast: Boolean,
    val dDay: String,
    val growthGraph: List<PresentationGrowthPoint>,
    val practiceRecords: PracticeRecords,
)
