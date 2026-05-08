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
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.feature.home.impl.R
import com.team.prezel.feature.home.impl.main.model.PresentationUiModel
import kotlinx.datetime.LocalDate

@Composable
internal fun PresentationSheet(
    practiceCount: Int,
    onClickPracticeRecording: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val itemModifier = Modifier.padding(horizontal = PrezelTheme.spacing.V20)

    HomeBottomSheetContent(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = PrezelTheme.spacing.V32),
    ) {
        HomeBottomSheetTitle(
            title = stringResource(R.string.feature_home_impl_bottom_sheet_content_title, practiceCount),
            modifier = itemModifier,
        )
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))
        PrezelButton(
            text = stringResource(R.string.feature_home_impl_practice_recording_action),
            modifier = itemModifier,
            onClick = onClickPracticeRecording,
        )
    }
}

@BasicPreview
@Composable
private fun PresentationContentPreview() {
    PrezelTheme {
        val presentation = PresentationUiModel(
            id = 1L,
            category = Category.PERSUASION,
            title = "설득하는 발표",
            date = LocalDate(2026, 10, 1),
            dDay = 3,
            practiceCount = 5,
        )

        Box(
            modifier = Modifier
                .height(100.dp)
                .padding(top = 16.dp),
        ) {
            PresentationSheet(
                practiceCount = presentation.practiceCount,
                onClickPracticeRecording = {},
            )
        }
    }
}
