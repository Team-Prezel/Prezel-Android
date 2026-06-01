package com.team.prezel.feature.report.impl.report.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelIconButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.chip.chip.ChipHierarchy
import com.team.prezel.core.designsystem.component.chip.chip.ChipSize
import com.team.prezel.core.designsystem.component.chip.chip.ChipState
import com.team.prezel.core.designsystem.component.chip.chip.ChipType
import com.team.prezel.core.designsystem.component.chip.chip.PrezelChip
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.report.model.PresentationInfoUiModel
import com.team.prezel.feature.report.impl.report.preview.ReportPreviewUpcomingUiState

@Composable
internal fun ReportHeaderContent(
    info: PresentationInfoUiModel,
    titleModifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable(info) { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V6)) {
        ReportCategoryChip(category = info.category)
        HeaderTitleRow(
            title = info.title,
            modifier = titleModifier,
            isExpanded = isExpanded,
            onExpandToggle = { isExpanded = !isExpanded },
        )
        HeaderMetadataSection(
            info = info,
            isExpanded = isExpanded,
        )
    }
}

@Composable
private fun ReportCategoryChip(category: Category) {
    PrezelChip(
        text = category.label(),
        iconResId = category.icon(),
        type = ChipType.FILLED,
        size = ChipSize.SMALL,
        state = ChipState.ACTIVE,
        hierarchy = ChipHierarchy.PRIMARY,
    )
}

@Composable
private fun HeaderTitleRow(
    title: String,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = PrezelTheme.typography.title1Bold,
            color = PrezelTheme.colors.textLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier.weight(1f),
        )

        PrezelIconButton(
            iconResId = if (isExpanded) PrezelIcons.ChevronUp else PrezelIcons.ChevronDown,
            size = ButtonSize.REGULAR,
            type = ButtonType.GHOST,
            hierarchy = ButtonHierarchy.SECONDARY,
            isUseRipple = false,
            onClick = onExpandToggle,
            modifier = Modifier.offset(x = 12.dp),
        )
    }
}

@Composable
private fun HeaderMetadataSection(
    info: PresentationInfoUiModel,
    isExpanded: Boolean,
) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16)) {
            HeaderInfoRow(label = stringResource(R.string.feature_report_impl_label_contents_info)) {
                HeaderMetaChipRow(info = info)
            }

            HeaderInfoRow(label = stringResource(R.string.feature_report_impl_label_analyzed_date)) {
                HeaderMetadataText(text = info.dateLabel())
            }

            HeaderInfoRow(label = stringResource(R.string.feature_report_impl_label_presentation_time)) {
                HeaderMetadataText(text = info.durationSeconds.toDurationString())
            }
        }
    }
}

private fun PresentationInfoUiModel.dateLabel(): String = analyzedAt.split("-").let { "${it[0]}년 ${it[1]}월 ${it[2]}일" }

private fun Int.toDurationString(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    return buildList {
        if (hours > 0) add("${hours}시간")
        if (minutes > 0) add("${minutes}분")
        if (seconds > 0 || isEmpty()) add("${seconds}초")
    }.joinToString(" ")
}

@Composable
private fun HeaderMetaChipRow(info: PresentationInfoUiModel) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V4),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V4),
    ) {
        HeaderMetaChip(text = info.purpose.label())
        HeaderMetaChip(text = info.style.label())
        HeaderMetaChip(text = info.audience.label())
    }
}

@Composable
private fun HeaderMetadataText(text: String) {
    Text(
        text = text,
        style = PrezelTheme.typography.body3Medium,
        color = PrezelTheme.colors.textRegular,
    )
}

@Composable
private fun HeaderInfoRow(
    label: String,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier.width(68.dp),
            style = PrezelTheme.typography.body3Medium,
            color = PrezelTheme.colors.textMedium,
        )
        Spacer(modifier = Modifier.width(PrezelTheme.spacing.V16))
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@Composable
private fun HeaderMetaChip(text: String) {
    PrezelChip(
        text = text,
        type = ChipType.FILLED,
        size = ChipSize.SMALL,
        hierarchy = ChipHierarchy.SECONDARY,
    )
}

@Composable
private fun Category.label(): String =
    stringResource(
        when (this) {
            Category.OFFER -> R.string.feature_report_impl_category_persuasion
            Category.EVENT -> R.string.feature_report_impl_category_event
            Category.EDUCATION -> R.string.feature_report_impl_category_education
            Category.WORK -> R.string.feature_report_impl_category_report
        },
    )

@Composable
private fun Category.icon(): Int =
    when (this) {
        Category.OFFER -> PrezelIcons.Hand
        Category.EVENT -> PrezelIcons.Balloon
        Category.EDUCATION -> PrezelIcons.College
        Category.WORK -> PrezelIcons.Company
    }

@Composable
private fun Purpose.label(): String =
    stringResource(
        when (this) {
            Purpose.INFO -> R.string.feature_report_impl_purpose_content_delivery
            Purpose.UNDERSTANDING -> R.string.feature_report_impl_purpose_improve_understanding
            Purpose.EMPATHY -> R.string.feature_report_impl_purpose_build_empathy
        },
    )

@Composable
private fun Style.label(): String =
    stringResource(
        when (this) {
            Style.FORMAL -> R.string.feature_report_impl_style_professional
            Style.FRIENDLY -> R.string.feature_report_impl_style_friendly
            Style.CALM -> R.string.feature_report_impl_style_calm
            Style.CASUAL -> R.string.feature_report_impl_style_comfortable
        },
    )

@Composable
private fun Audience.label(): String =
    stringResource(
        when (this) {
            Audience.GENERAL -> R.string.feature_report_impl_audience_general
            Audience.PROFESSIONAL -> R.string.feature_report_impl_audience_expert
            Audience.TEAMMATE -> R.string.feature_report_impl_audience_teammates
        },
    )

@BasicPreview
@Composable
private fun ReportHeaderContentPreview() {
    PrezelTheme {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
        ) {
            ReportHeaderContent(info = ReportPreviewUpcomingUiState.presentationInfo)
        }
    }
}
