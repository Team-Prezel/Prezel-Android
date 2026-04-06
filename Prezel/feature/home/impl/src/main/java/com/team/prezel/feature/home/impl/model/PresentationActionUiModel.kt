package com.team.prezel.feature.home.impl.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.team.prezel.feature.home.impl.R

internal data class PracticeActionUiModel(
    val title: String,
    val actionText: String,
    val type: PracticeActionType,
    val presentation: PresentationUiModel? = null,
)

internal enum class PracticeActionType {
    ADD_PRESENTATION,
    ANALYZE_PRESENTATION,
    WRITE_FEEDBACK,
}

@Composable
internal fun emptyPracticeActionUiModel(): PracticeActionUiModel =
    PracticeActionUiModel(
        title = stringResource(R.string.feature_home_impl_add_presentation_title),
        actionText = stringResource(R.string.feature_home_impl_add_presentation_action),
        type = PracticeActionType.ADD_PRESENTATION,
    )

@Composable
internal fun PresentationUiModel.toPracticeActionUiModel(): PracticeActionUiModel {
    val isPastPresentation = dDay() < 0

    return if (isPastPresentation) {
        PracticeActionUiModel(
            title = stringResource(R.string.feature_home_impl_write_feedback_title, title),
            actionText = stringResource(R.string.feature_home_impl_write_feedback_action),
            type = PracticeActionType.WRITE_FEEDBACK,
            presentation = this,
        )
    } else {
        PracticeActionUiModel(
            title = stringResource(R.string.feature_home_impl_analyze_presentation_title),
            actionText = stringResource(R.string.feature_home_impl_analyze_presentation_action),
            type = PracticeActionType.ANALYZE_PRESENTATION,
            presentation = this,
        )
    }
}
