package com.team.prezel.feature.report.impl.report.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style

@Immutable
internal data class PresentationInfoUiModel(
    val category: Category,
    val title: String,
    val purpose: Purpose,
    val style: Style,
    val audience: Audience,
    val analyzedAt: String,
    val durationSeconds: Int,
)
