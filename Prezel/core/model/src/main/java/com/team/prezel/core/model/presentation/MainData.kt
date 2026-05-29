package com.team.prezel.core.model.presentation

import kotlinx.datetime.LocalDate

data class MainData(
    val presentationId: Long,
    val title: String,
    val type: String,
    val presentationDate: LocalDate,
    val isPast: Boolean,
    val dDay: String,
    val accuracyScoreChange: Int,
    val scriptMatchRateChange: Int,
    val growthGraph: List<PresentationGrowthPoint>,
)
