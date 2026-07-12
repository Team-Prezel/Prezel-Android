package com.team.prezel.feature.home.impl.main.component.body

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.ui.component.PracticeCard
import com.team.prezel.core.ui.component.graph.CardGraph
import com.team.prezel.feature.home.impl.R
import com.team.prezel.feature.home.impl.main.model.PracticeRecordsUiModel
import com.team.prezel.feature.home.impl.main.model.PresentationUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate

@Composable
internal fun PresentationSheet(
    presentation: PresentationUiModel,
    onClickPracticeRecording: () -> Unit,
    onClickCardGraphItemIndex: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeBottomSheetContent(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = PrezelTheme.spacing.V32, horizontal = PrezelTheme.spacing.V20),
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
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        when (presentation) {
            is PresentationUiModel.Past -> {
                if (presentation.growthGraphData.graphItems.isEmpty()) return@HomeBottomSheetContent

                HomeBottomSheetTitle(title = stringResource(R.string.feature_home_impl_bottom_sheet_past_graph_title))
                CardGraph(
                    items = presentation.growthGraphData.graphItems,
                    selectedItemIndex = presentation.growthGraphData.selectedItemIndex,
                    onSelectItem = { index -> onClickCardGraphItemIndex(index) },
                )
            }

            is PresentationUiModel.Upcoming -> {
                HomeBottomSheetTitle(title = stringResource(R.string.feature_home_impl_bottom_sheet_upcoming_keywords_title))
            }
        }
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V36))
    }
}

@BasicPreview
@Composable
private fun PresentationContentPreview() {
    PrezelTheme {
        val presentation = PresentationUiModel.Upcoming(
            id = 1L,
            category = Category.OFFER,
            title = "설득하는 발표",
            date = LocalDate(2026, 10, 1),
            dDay = "D-3",
            practiceRecords = PracticeRecordsUiModel(
                practicedDates = List(5) { LocalDate(2026, 9, 26 + it) },
                startDate = LocalDate(2026, 9, 26),
                endDate = LocalDate(2026, 10, 1),
            ),
            curations = persistentListOf(),
        )

        Box(modifier = Modifier.padding(top = 16.dp)) {
            PresentationSheet(
                presentation = presentation,
                onClickPracticeRecording = {},
                onClickCardGraphItemIndex = {},
            )
        }
    }
}
