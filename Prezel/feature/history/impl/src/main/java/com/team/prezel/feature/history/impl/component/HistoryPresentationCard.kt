package com.team.prezel.feature.history.impl.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.PresentationAnalysisStatus
import com.team.prezel.feature.history.impl.R
import com.team.prezel.feature.history.impl.model.HistoryUiModel
import kotlinx.datetime.LocalDate

@Composable
internal fun HistoryPresentationCard(
    item: HistoryUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    PrezelTouchArea(
        onClick = onClick,
        shape = PrezelTheme.shapes.V8,
        enabled = enabled,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape = PrezelTheme.shapes.V8)
                .background(PrezelTheme.colors.bgRegular),
        ) {
            HistoryPresentationStatusTrack(
                category = item.category,
                status = item.analysisStatus,
            )
            HistoryPresentationCardBody(item = item)
        }
    }
}

@Composable
private fun HistoryPresentationStatusTrack(
    category: Category,
    status: PresentationAnalysisStatus,
    modifier: Modifier = Modifier,
) {
    val style = status.trackStyle()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                drawRect(color = style.containerColor)
                style.progressColor?.let { progressColor ->
                    drawRect(
                        color = progressColor,
                        size = Size(
                            width = size.width * style.progressFraction,
                            height = size.height,
                        ),
                    )
                }
            }.padding(horizontal = PrezelTheme.spacing.V12, vertical = PrezelTheme.spacing.V8),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = category.iconResId()),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = style.contentColor,
            )
            Spacer(modifier = Modifier.width(PrezelTheme.spacing.V2))
            Text(
                text = stringResource(category.labelResId()),
                style = PrezelTheme.typography.caption2Regular,
                color = style.contentColor,
            )
        }

        status.statusLabelResId()?.let { labelResId ->
            Text(
                text = stringResource(labelResId),
                style = PrezelTheme.typography.caption2Medium,
                color = style.contentColor,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun HistoryPresentationCardBody(
    item: HistoryUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(PrezelTheme.spacing.V14),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.title,
                modifier = Modifier.weight(1f),
                style = PrezelTheme.typography.body2Bold,
                color = PrezelTheme.colors.textLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.dDay,
                style = PrezelTheme.typography.title2Bold,
                color = PrezelTheme.colors.textLarge,
            )
        }
        Text(
            text = item.dateLabel,
            style = PrezelTheme.typography.caption1Regular,
            color = PrezelTheme.colors.textSmall,
            maxLines = 1,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@StringRes
private fun Category.labelResId(): Int =
    when (this) {
        Category.EDUCATION -> R.string.feature_history_impl_category_education
        Category.WORK -> R.string.feature_history_impl_category_work
        Category.OFFER -> R.string.feature_history_impl_category_offer
        Category.EVENT -> R.string.feature_history_impl_category_event
    }

@DrawableRes
private fun Category.iconResId(): Int =
    when (this) {
        Category.EDUCATION -> PrezelIcons.College
        Category.WORK -> PrezelIcons.Company
        Category.OFFER -> PrezelIcons.Hand
        Category.EVENT -> PrezelIcons.Balloon
    }

@Composable
private fun PresentationAnalysisStatus.trackStyle(): HistoryPresentationStatusTrackStyle =
    when (this) {
        PresentationAnalysisStatus.IN_PROCESS -> HistoryPresentationStatusTrackStyle(
            containerColor = PrezelTheme.colors.bgDisabled,
            contentColor = PrezelTheme.colors.textRegular,
            progressColor = PrezelTheme.colors.bgLarge,
            progressFraction = 0.5f,
        )

        PresentationAnalysisStatus.COMPLETE,
        PresentationAnalysisStatus.DEFAULT,
        -> HistoryPresentationStatusTrackStyle(
            containerColor = PrezelTheme.colors.interactiveXSmall,
            contentColor = PrezelTheme.colors.interactiveRegular,
        )

        PresentationAnalysisStatus.FAIL -> HistoryPresentationStatusTrackStyle(
            containerColor = PrezelTheme.colors.feedbackBadSmall,
            contentColor = PrezelTheme.colors.feedbackBadRegular,
        )
    }

@StringRes
private fun PresentationAnalysisStatus.statusLabelResId(): Int? =
    when (this) {
        PresentationAnalysisStatus.IN_PROCESS -> R.string.feature_history_impl_status_in_process
        PresentationAnalysisStatus.COMPLETE -> R.string.feature_history_impl_status_complete
        PresentationAnalysisStatus.DEFAULT -> null
        PresentationAnalysisStatus.FAIL -> R.string.feature_history_impl_status_fail
    }

private data class HistoryPresentationStatusTrackStyle(
    val containerColor: Color,
    val contentColor: Color,
    val progressColor: Color? = null,
    val progressFraction: Float = 0f,
)

@BasicPreview
@Composable
private fun HistoryPresentationCardPreview() {
    PrezelTheme {
        Column(
            modifier = Modifier
                .background(color = PrezelTheme.colors.bgMedium)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V14),
        ) {
            listOf(
                PresentationAnalysisStatus.IN_PROCESS,
                PresentationAnalysisStatus.COMPLETE,
                PresentationAnalysisStatus.DEFAULT,
                PresentationAnalysisStatus.FAIL,
            ).forEach { status ->
                HistoryPresentationCard(
                    item = HistoryUiModel(
                        presentationId = status.ordinal.toLong(),
                        title = "캡스톤서비스기획 중간고사 발표",
                        presentationDate = LocalDate(2025, 10, 20),
                        category = Category.EDUCATION,
                        dDay = "D-5",
                        analysisStatus = status,
                    ),
                    onClick = { },
                )
            }
        }
    }
}
