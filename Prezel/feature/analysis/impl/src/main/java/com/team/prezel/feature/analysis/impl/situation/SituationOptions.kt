package com.team.prezel.feature.analysis.impl.situation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.feature.analysis.impl.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
internal data class SituationCategoryOption(
    val value: Category,
    val title: String,
    val description: String,
    @param:DrawableRes val iconResId: Int,
)

@Immutable
internal data class SituationChipOption<T : Enum<T>>(
    val value: T,
    val text: String,
)

@Immutable
internal data class SituationChipContentOption(
    val text: String,
    val selected: Boolean,
)

@Composable
internal fun categoryOptions(): ImmutableList<SituationCategoryOption> =
    Category.entries
        .map { category ->
            SituationCategoryOption(
                value = category,
                title = stringResource(category.titleResId),
                description = stringResource(category.descriptionResId),
                iconResId = category.iconResId,
            )
        }.toImmutableList()

@Composable
internal fun purposeOptions(): ImmutableList<SituationChipOption<Purpose>> =
    Purpose.entries
        .map { purpose ->
            SituationChipOption(
                value = purpose,
                text = stringResource(purpose.titleResId),
            )
        }.toImmutableList()

@Composable
internal fun styleOptions(): ImmutableList<SituationChipOption<Style>> =
    Style.entries
        .map { style ->
            SituationChipOption(
                value = style,
                text = stringResource(style.titleResId),
            )
        }.toImmutableList()

@Composable
internal fun audienceOptions(): ImmutableList<SituationChipOption<Audience>> =
    Audience.entries
        .map { audience ->
            SituationChipOption(
                value = audience,
                text = stringResource(audience.titleResId),
            )
        }.toImmutableList()

private val Category.titleResId: Int
    @StringRes get() = when (this) {
        Category.PERSUASION -> R.string.feature_analysis_impl_situation_category_persuasion
        Category.EVENT -> R.string.feature_analysis_impl_situation_category_event
        Category.EDUCATION -> R.string.feature_analysis_impl_situation_category_academic
        Category.REPORT -> R.string.feature_analysis_impl_situation_category_business
    }

private val Category.descriptionResId: Int
    @StringRes get() = when (this) {
        Category.PERSUASION -> R.string.feature_analysis_impl_situation_category_persuasion_description
        Category.EVENT -> R.string.feature_analysis_impl_situation_category_event_description
        Category.EDUCATION -> R.string.feature_analysis_impl_situation_category_academic_description
        Category.REPORT -> R.string.feature_analysis_impl_situation_category_business_description
    }

private val Category.iconResId: Int
    @DrawableRes get() = when (this) {
        Category.PERSUASION -> PrezelIcons.Hand
        Category.EVENT -> PrezelIcons.Balloon
        Category.EDUCATION -> PrezelIcons.College
        Category.REPORT -> PrezelIcons.Company
    }

private val Purpose.titleResId: Int
    @StringRes get() = when (this) {
        Purpose.CONTENT_DELIVERY -> R.string.feature_analysis_impl_situation_purpose_content_delivery
        Purpose.IMPROVE_UNDERSTANDING -> R.string.feature_analysis_impl_situation_purpose_improve_understanding
        Purpose.BUILD_EMPATHY -> R.string.feature_analysis_impl_situation_purpose_build_empathy
    }

private val Style.titleResId: Int
    @StringRes get() = when (this) {
        Style.PROFESSIONAL -> R.string.feature_analysis_impl_situation_style_professional
        Style.FRIENDLY -> R.string.feature_analysis_impl_situation_style_friendly
        Style.CALM -> R.string.feature_analysis_impl_situation_style_calm
        Style.COMFORTABLE -> R.string.feature_analysis_impl_situation_style_comfortable
    }

private val Audience.titleResId: Int
    @StringRes get() = when (this) {
        Audience.GENERAL_AUDIENCE -> R.string.feature_analysis_impl_situation_audience_general
        Audience.EXPERT -> R.string.feature_analysis_impl_situation_audience_expert
        Audience.TEAMMATES -> R.string.feature_analysis_impl_situation_audience_team
    }
