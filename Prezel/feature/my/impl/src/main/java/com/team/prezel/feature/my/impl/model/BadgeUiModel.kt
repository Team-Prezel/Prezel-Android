package com.team.prezel.feature.my.impl.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class BadgeUiModel(
    val code: String,
    val title: String,
    val imageUrl: String,
    val isAchieved: Boolean,
)
