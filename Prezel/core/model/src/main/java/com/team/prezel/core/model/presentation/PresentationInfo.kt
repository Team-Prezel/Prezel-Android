package com.team.prezel.core.model.presentation

import kotlinx.datetime.LocalDate

data class PresentationInfo(
    val id: Long,
    val title: String,
    val presentationDate: LocalDate,
    val category: Category,
    val dDay: String,
    val analysisStatus: PresentationAnalysisStatus = PresentationAnalysisStatus.DEFAULT,
)
