package com.team.prezel.feature.home.impl.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.team.prezel.feature.home.impl.R
import com.team.prezel.core.designsystem.R as DSR

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
        CategoryUiModel.PERSUASION -> DSR.drawable.core_designsystem_section_title_hand
        CategoryUiModel.EVENT -> DSR.drawable.core_designsystem_section_title_event
        CategoryUiModel.EDUCATION -> DSR.drawable.core_designsystem_section_title_college
        CategoryUiModel.REPORT -> DSR.drawable.core_designsystem_section_title_company
    }
