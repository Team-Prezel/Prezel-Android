package com.team.prezel.feature.history.impl.mapper

import androidx.annotation.StringRes
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.feature.history.impl.R

@StringRes
internal fun Category.labelResId(): Int =
    when (this) {
        Category.PERSUASION -> R.string.feature_history_impl_category_persuasion
        Category.EVENT -> R.string.feature_history_impl_category_event
        Category.EDUCATION -> R.string.feature_history_impl_category_education
        Category.REPORT -> R.string.feature_history_impl_category_report
    }

@StringRes
internal fun Purpose.labelResId(): Int =
    when (this) {
        Purpose.CONTENT_DELIVERY -> R.string.feature_history_impl_purpose_content_delivery
        Purpose.IMPROVE_UNDERSTANDING -> R.string.feature_history_impl_purpose_improve_understanding
        Purpose.BUILD_EMPATHY -> R.string.feature_history_impl_purpose_build_empathy
    }

@StringRes
internal fun Style.labelResId(): Int =
    when (this) {
        Style.PROFESSIONAL -> R.string.feature_history_impl_style_professional
        Style.FRIENDLY -> R.string.feature_history_impl_style_friendly
        Style.CALM -> R.string.feature_history_impl_style_calm
        Style.COMFORTABLE -> R.string.feature_history_impl_style_comfortable
    }

@StringRes
internal fun Audience.labelResId(): Int =
    when (this) {
        Audience.GENERAL_AUDIENCE -> R.string.feature_history_impl_audience_general
        Audience.EXPERT -> R.string.feature_history_impl_audience_expert
        Audience.TEAMMATES -> R.string.feature_history_impl_audience_teammates
    }
