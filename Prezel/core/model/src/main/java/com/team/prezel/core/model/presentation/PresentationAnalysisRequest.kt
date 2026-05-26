package com.team.prezel.core.model.presentation

import kotlinx.datetime.LocalDateTime

data class PresentationAnalysisRequest(
    val title: String,
    val dateTime: LocalDateTime,
    val category: Category,
    val purpose: Purpose,
    val style: Style,
    val audience: Audience,
    val script: String?,
    val audioFilePath: String,
)
