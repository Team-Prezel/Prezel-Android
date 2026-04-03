package com.team.prezel.feature.home.impl.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable
data class CategoryUiModel(
    @param:DrawableRes val iconResId: Int? = null,
    val name: String,
)
