package com.team.prezel.core.model.presentation

import kotlinx.datetime.LocalDate

data class PresentationInfo(
    val id: Long,
    val title: String,
    val presentationDate: LocalDate,
    val category: Category,
    val purpose: Purpose,
    val style: Style,
    val audience: Audience,
    val dDay: String,
)
