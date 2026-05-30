package com.team.prezel.core.model.presentation

import kotlinx.datetime.LocalDate

data class PracticeRecords(
    val dates: List<LocalDate>,
    val startDate: LocalDate,
    val endDate: LocalDate,
)
