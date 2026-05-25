package com.team.prezel.feature.report.impl.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
data class PracticeUiModel(
    val date: LocalDate,
    val isPracticed: Boolean,
)
