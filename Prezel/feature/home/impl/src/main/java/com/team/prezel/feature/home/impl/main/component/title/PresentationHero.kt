package com.team.prezel.feature.home.impl.main.component.title

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.feature.home.impl.R
import com.team.prezel.feature.home.impl.main.model.GrowthGraphData
import com.team.prezel.feature.home.impl.main.model.PracticeRecordsUiModel
import com.team.prezel.feature.home.impl.main.model.PresentationUiModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

@Composable
internal fun PresentationHero(
    presentation: PresentationUiModel,
    onClickAnalyzePresentation: (PresentationUiModel) -> Unit,
    onClickWriteFeedback: (PresentationUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeHeroLayout(
        backgroundResId = presentation.category.backgroundResId(),
        modifier = modifier,
    ) {
        HomeCategoryChip(
            text = stringResource(id = presentation.category.labelResId()),
        )

        Spacer(modifier = Modifier.weight(1f))

        HomePresentationDate(date = presentation.date)

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))

        HomePresentationTitleRow(presentation = presentation)

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))

        PracticeActionCard(
            title = if (presentation.isPastPresentation) {
                stringResource(R.string.feature_home_impl_write_feedback_title, presentation.title)
            } else {
                stringResource(R.string.feature_home_impl_analyze_presentation_title)
            },
            actionText = if (presentation.isPastPresentation) {
                stringResource(R.string.feature_home_impl_write_feedback_action)
            } else {
                stringResource(R.string.feature_home_impl_analyze_presentation_action)
            },
            titleColor = PrezelTheme.colors.textMedium,
            onClick = {
                if (presentation.isPastPresentation) onClickWriteFeedback(presentation) else onClickAnalyzePresentation(presentation)
            },
        )
    }
}

@Composable
private fun HomeCategoryChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = PrezelTheme.colors.interactiveRegular,
        style = PrezelTheme.typography.caption2Regular,
        modifier = modifier
            .clip(PrezelTheme.shapes.V4)
            .background(PrezelTheme.colors.bgRegular)
            .padding(
                horizontal = PrezelTheme.spacing.V6,
                vertical = PrezelTheme.spacing.V4,
            ),
    )
}

@Composable
private fun HomePresentationDate(date: LocalDate) {
    Text(
        text = stringResource(
            R.string.feature_home_impl_presentation_date,
            date.year,
            date.month.number,
            date.day,
        ),
        color = PrezelTheme.colors.textRegular,
        style = PrezelTheme.typography.body3Regular,
    )
}

@Composable
private fun HomePresentationTitleRow(presentation: PresentationUiModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = presentation.title,
            modifier = Modifier.weight(1f),
            color = PrezelTheme.colors.textLarge,
            style = PrezelTheme.typography.title1Bold,
        )
        Text(
            text = presentation.dDay,
            color = PrezelTheme.colors.interactiveRegular,
            style = PrezelTheme.typography.title1ExtraBold,
        )
    }
}

@StringRes
private fun Category.labelResId(): Int =
    when (this) {
        Category.OFFER -> R.string.feature_home_impl_category_persuasion
        Category.EVENT -> R.string.feature_home_impl_category_event
        Category.EDUCATION -> R.string.feature_home_impl_category_education
        Category.WORK -> R.string.feature_home_impl_category_report
    }

@DrawableRes
private fun Category.backgroundResId(): Int =
    when (this) {
        Category.OFFER -> R.drawable.feature_home_impl_section_title_hand
        Category.EVENT -> R.drawable.feature_home_impl_section_title_event
        Category.EDUCATION -> R.drawable.feature_home_impl_section_title_college
        Category.WORK -> R.drawable.feature_home_impl_section_title_company
    }

@BasicPreview
@Composable
private fun HomePresentationPagePreview() {
    PrezelTheme {
        PresentationHero(
            presentation = PresentationUiModel.Upcoming(
                id = 1L,
                category = Category.OFFER,
                title = "설득하는 발표",
                date = LocalDate(2026, 10, 1),
                dDay = "D-3",
                practiceRecords = PracticeRecordsUiModel(
                    practicedDates = listOf(LocalDate(2026, 9, 28)),
                    startDate = LocalDate(2026, 9, 26),
                    endDate = LocalDate(2026, 10, 1),
                ),
            ),
            onClickAnalyzePresentation = {},
            onClickWriteFeedback = {},
        )
    }
}

@BasicPreview
@Composable
private fun HomePresentationPagePastPreview() {
    PrezelTheme {
        PresentationHero(
            presentation = PresentationUiModel.Past(
                id = 2L,
                category = Category.EDUCATION,
                title = "교육 발표",
                date = LocalDate(2026, 9, 20),
                dDay = "D+5",
                practiceRecords = PracticeRecordsUiModel(
                    practicedDates = listOf(LocalDate(2026, 9, 18), LocalDate(2026, 9, 19)),
                    startDate = LocalDate(2026, 9, 15),
                    endDate = LocalDate(2026, 9, 20),
                ),
                growthGraphData = GrowthGraphData(items = emptyList(), selectedItemIndex = 0),
            ),
            onClickAnalyzePresentation = {},
            onClickWriteFeedback = {},
        )
    }
}
