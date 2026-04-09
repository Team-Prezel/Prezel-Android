package com.team.prezel.feature.home.impl.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.team.prezel.feature.home.impl.R

enum class CategoryUiModel {
    PERSUASION,
    EVENT,
    EDUCATION,
    REPORT,
}

@Composable
fun CategoryUiModel.label(): String =
    when (this) {
        CategoryUiModel.PERSUASION -> stringResource(R.string.feature_home_impl_category_persuasion)
        CategoryUiModel.EVENT -> stringResource(R.string.feature_home_impl_category_event)
        CategoryUiModel.EDUCATION -> stringResource(R.string.feature_home_impl_category_education)
        CategoryUiModel.REPORT -> stringResource(R.string.feature_home_impl_category_report)
    }

@DrawableRes
fun CategoryUiModel.backgroundRes(): Int =
    when (this) {
        CategoryUiModel.PERSUASION -> R.drawable.feature_home_impl_section_title_hand
        CategoryUiModel.EVENT -> R.drawable.feature_home_impl_section_title_event
        CategoryUiModel.EDUCATION -> R.drawable.feature_home_impl_section_title_college
        CategoryUiModel.REPORT -> R.drawable.feature_home_impl_section_title_company
    }
