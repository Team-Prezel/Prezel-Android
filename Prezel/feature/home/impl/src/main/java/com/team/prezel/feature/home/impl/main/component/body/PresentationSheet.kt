package com.team.prezel.feature.home.impl.main.component.body

import androidx.annotation.StringRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.chip.chip.ChipSize
import com.team.prezel.core.designsystem.component.chip.chip.ChipType
import com.team.prezel.core.designsystem.component.chip.chip.PrezelChip
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.ui.component.PracticeCard
import com.team.prezel.core.ui.component.graph.CardGraph
import com.team.prezel.feature.home.impl.R
import com.team.prezel.feature.home.impl.main.model.CurationUiModel
import com.team.prezel.feature.home.impl.main.model.PracticeRecordsUiModel
import com.team.prezel.feature.home.impl.main.model.PresentationUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate

@Composable
internal fun PresentationSheet(
    presentation: PresentationUiModel,
    onClickPracticeRecording: () -> Unit,
    onClickCardGraphItemIndex: (index: Int) -> Unit,
    onCurationLinkOpenFailed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeBottomSheetContent(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = PrezelTheme.spacing.V32, horizontal = PrezelTheme.spacing.V20),
    ) {
        PresentationPracticeSection(
            presentation = presentation,
            onClickPracticeRecording = onClickPracticeRecording,
        )
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        when (presentation) {
            is PresentationUiModel.Past -> {
                if (presentation.growthGraphData.graphItems.isEmpty()) return@HomeBottomSheetContent

                PastPresentationSection(
                    presentation = presentation,
                    onClickCardGraphItemIndex = onClickCardGraphItemIndex,
                )
            }

            is PresentationUiModel.Upcoming -> UpcomingPresentationSection(
                presentation = presentation,
                onCurationLinkOpenFailed = onCurationLinkOpenFailed,
            )
        }
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V36))
    }
}

@Composable
private fun PresentationPracticeSection(
    presentation: PresentationUiModel,
    onClickPracticeRecording: () -> Unit,
) {
    HomeBottomSheetTitle(
        title = if (presentation.practiceCount == 0) {
            stringResource(R.string.feature_home_impl_empty_sheet_practice_card_title)
        } else {
            stringResource(R.string.feature_home_impl_bottom_sheet_content_title, presentation.practiceCount)
        },
    )
    PracticeCard(
        dDay = presentation.practiceRecords.endDate,
        items = presentation.practiceRecords.practices,
        showActionButton = !presentation.isPastPresentation,
        onClickAction = onClickPracticeRecording,
    )
}

@Composable
private fun PastPresentationSection(
    presentation: PresentationUiModel.Past,
    onClickCardGraphItemIndex: (index: Int) -> Unit,
) {
    HomeBottomSheetTitle(title = stringResource(R.string.feature_home_impl_bottom_sheet_past_graph_title))
    CardGraph(
        items = presentation.growthGraphData.graphItems,
        selectedItemIndex = presentation.growthGraphData.selectedItemIndex,
        onSelectItem = onClickCardGraphItemIndex,
    )
}

@Composable
private fun UpcomingPresentationSection(
    presentation: PresentationUiModel.Upcoming,
    onCurationLinkOpenFailed: () -> Unit,
) {
    val firstCuration = presentation.curations.firstOrNull() ?: return

    HomeBottomSheetTitle(title = firstCuration.guideMessage)
    CurationMetadataRow(
        presentationTitle = presentation.title,
        curation = firstCuration,
    )
    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
    Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16)) {
        presentation.curations.forEach { curation ->
            CurationCard(
                curation = curation,
                onLinkOpenFailed = onCurationLinkOpenFailed,
            )
        }
    }
}

@Composable
private fun CurationMetadataRow(
    presentationTitle: String,
    curation: CurationUiModel,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .wrapContentWidth(align = Alignment.Start, unbounded = true)
                .requiredWidth(maxWidth + PrezelTheme.spacing.V20)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = presentationTitle,
                color = PrezelTheme.colors.textMedium,
                style = PrezelTheme.typography.caption2Regular,
            )
            VerticalDivider(
                modifier = Modifier.height(PrezelTheme.spacing.V24),
                color = PrezelTheme.colors.borderSmall,
            )
            listOf(
                curation.category.labelResId(),
                curation.purpose.labelResId(),
                curation.style.labelResId(),
                curation.audience.labelResId(),
            ).forEach { labelResId ->
                PrezelChip(
                    text = stringResource(labelResId),
                    type = ChipType.FILLED,
                    size = ChipSize.SMALL,
                )
            }
        }
    }
}

@BasicPreview
@Composable
private fun PresentationContentPreview() {
    PrezelTheme {
        val presentation = PresentationUiModel.Upcoming(
            id = 1L,
            category = Category.OFFER,
            title = "사용자가 입력한 발표 제목",
            date = LocalDate(2026, 10, 1),
            dDay = "D-3",
            practiceRecords = PracticeRecordsUiModel(
                practicedDates = List(5) { LocalDate(2026, 9, 26 + it) },
                startDate = LocalDate(2026, 9, 26),
                endDate = LocalDate(2026, 10, 1),
            ),
            curations = persistentListOf(
                CurationUiModel(
                    guideMessage = "발표 흐름을 키워드별로 정리해보세요",
                    category = Category.EDUCATION,
                    purpose = Purpose.INFO,
                    style = Style.FORMAL,
                    audience = Audience.GENERAL,
                    materialType = "아티클",
                    title = "제목이 한 줄이라면 이래요",
                    sourceChannel = "계정 이름",
                    linkUrl = "https://example.com/article",
                    imageUrl = "https://picsum.photos/280/160?random=1",
                ),
                CurationUiModel(
                    guideMessage = "발표 흐름을 키워드별로 정리해보세요",
                    category = Category.EDUCATION,
                    purpose = Purpose.INFO,
                    style = Style.FORMAL,
                    audience = Audience.GENERAL,
                    materialType = "영상",
                    title = "제목이 두 줄로 넘어가면 자연스럽게 말줄임표로 전환돼요",
                    sourceChannel = "프레젠테이션 채널",
                    linkUrl = "https://example.com/video",
                    imageUrl = "https://picsum.photos/280/160?random=2",
                ),
                CurationUiModel(
                    guideMessage = "발표 흐름을 키워드별로 정리해보세요",
                    category = Category.EDUCATION,
                    purpose = Purpose.INFO,
                    style = Style.FORMAL,
                    audience = Audience.GENERAL,
                    materialType = "아티클",
                    title = "청중을 설득하는 발표 구성 방법",
                    sourceChannel = "발표 연구소",
                    linkUrl = "https://example.com/presentation",
                    imageUrl = "https://picsum.photos/280/160?random=3",
                ),
            ),
        )

        Box(modifier = Modifier.padding(top = 16.dp)) {
            PresentationSheet(
                presentation = presentation,
                onClickPracticeRecording = {},
                onClickCardGraphItemIndex = {},
                onCurationLinkOpenFailed = {},
            )
        }
    }
}

@StringRes
private fun Category.labelResId(): Int =
    when (this) {
        Category.EDUCATION -> R.string.feature_home_impl_category_education
        Category.WORK -> R.string.feature_home_impl_category_report
        Category.OFFER -> R.string.feature_home_impl_category_persuasion
        Category.EVENT -> R.string.feature_home_impl_category_event
    }

@StringRes
private fun Purpose.labelResId(): Int =
    when (this) {
        Purpose.INFO -> R.string.feature_home_impl_purpose_info
        Purpose.UNDERSTANDING -> R.string.feature_home_impl_purpose_understanding
        Purpose.EMPATHY -> R.string.feature_home_impl_purpose_empathy
    }

@StringRes
private fun Style.labelResId(): Int =
    when (this) {
        Style.FORMAL -> R.string.feature_home_impl_style_formal
        Style.FRIENDLY -> R.string.feature_home_impl_style_friendly
        Style.CALM -> R.string.feature_home_impl_style_calm
        Style.CASUAL -> R.string.feature_home_impl_style_casual
    }

@StringRes
private fun Audience.labelResId(): Int =
    when (this) {
        Audience.GENERAL -> R.string.feature_home_impl_audience_general
        Audience.PROFESSIONAL -> R.string.feature_home_impl_audience_professional
        Audience.TEAMMATE -> R.string.feature_home_impl_audience_teammate
    }
