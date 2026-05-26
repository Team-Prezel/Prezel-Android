package com.team.prezel.feature.report.impl.component.body

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelDividerType
import com.team.prezel.core.designsystem.component.PrezelHorizontalDivider
import com.team.prezel.core.designsystem.component.PrezelVerticalDivider
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.feedback.tooltip.PrezelTooltipBox
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.graph.CardGraph
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.component.common.ReportSection
import com.team.prezel.feature.report.impl.component.common.toPercentLabel
import com.team.prezel.feature.report.impl.model.GrowthGraphData
import com.team.prezel.feature.report.impl.model.GrowthGraphItemUiModel
import com.team.prezel.feature.report.impl.preview.ReportPreviewUpcomingUiState
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun GrowthGraphSection(
    growthGraphData: GrowthGraphData,
    onCardIndexChange: (Int) -> Unit,
    onReRecordingClick: () -> Unit,
) {
    ReportSection(
        title = { GrowthGraphSectionTitle(onReRecordingClick = onReRecordingClick) },
    ) {
        GrowthGraphContent(
            growthGraphData = growthGraphData,
            onCardIndexChange = onCardIndexChange,
        )
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
        GrowthGraphSummary(selectedItem = growthGraphData.selectedItem)
    }
}

@Composable
private fun GrowthGraphSectionTitle(onReRecordingClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.feature_report_impl_section_growth_graph),
                style = PrezelTheme.typography.title2Bold,
                color = PrezelTheme.colors.textLarge,
            )
            Spacer(modifier = Modifier.width(PrezelTheme.spacing.V4))
            PrezelTooltipBox(
                text = stringResource(R.string.feature_report_impl_growth_graph_tooltip),
            ) {
                Icon(
                    painter = painterResource(PrezelIcons.InfoCircleOutlined),
                    contentDescription = null,
                    tint = PrezelTheme.colors.iconRegular,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
        PrezelButton(
            text = stringResource(R.string.feature_report_impl_re_recording),
            iconResId = PrezelIcons.Mic,
            type = ButtonType.OUTLINED,
            size = ButtonSize.XSMALL,
            hierarchy = ButtonHierarchy.SECONDARY,
            isRounded = true,
            onClick = onReRecordingClick,
        )
    }
}

@Composable
private fun GrowthGraphContent(
    growthGraphData: GrowthGraphData,
    onCardIndexChange: (Int) -> Unit,
) {
    if (growthGraphData.items.isEmpty()) {
        EmptyGrowthGraph()
        return
    }

    CardGraph(
        items = growthGraphData.graphItems,
        selectedItemIndex = growthGraphData.selectedItemIndex,
        modifier = Modifier.fillMaxWidth(),
        onSelectItem = onCardIndexChange,
        showDetail = false,
        useContainerStyle = false,
    )
}

@Composable
private fun EmptyGrowthGraph() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(320 / 128f),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.feature_report_impl_empty_growth_graph),
                style = PrezelTheme.typography.body3Regular,
                color = PrezelTheme.colors.textSmall,
                textAlign = TextAlign.Center,
            )
        }
        PrezelHorizontalDivider(
            type = PrezelDividerType.THICK,
            color = PrezelTheme.colors.borderSmall,
        )
    }
}

@Composable
private fun GrowthGraphSummary(selectedItem: GrowthGraphItemUiModel?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = PrezelTheme.shapes.V8)
            .background(color = PrezelTheme.colors.bgMedium),
        horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GrowthResultCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.feature_report_impl_label_speech),
            value = selectedItem?.accuracyScore?.toPercentLabel() ?: "-%",
            thumbnailColor = PrezelTheme.colors.feedbackGoodRegular,
        )
        PrezelVerticalDivider(
            color = PrezelTheme.colors.borderRegular,
            type = PrezelDividerType.THICK,
            modifier = Modifier.height(40.dp),
        )
        GrowthResultCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.feature_report_impl_label_script_match),
            value = selectedItem?.scriptMatchRate?.toPercentLabel() ?: "-%",
            thumbnailColor = PrezelTheme.colors.feedbackWarningRegular,
        )
    }
}

@Composable
private fun GrowthResultCard(
    title: String,
    value: String,
    thumbnailColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = PrezelTheme.spacing.V12,
                vertical = PrezelTheme.spacing.V14,
            ),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(PrezelTheme.shapes.V1000)
                    .background(color = thumbnailColor),
            )
            Spacer(modifier = Modifier.width(PrezelTheme.spacing.V8))
            Text(
                text = title,
                style = PrezelTheme.typography.caption1Regular,
                color = PrezelTheme.colors.textRegular,
            )
        }
        Text(
            text = value,
            style = PrezelTheme.typography.title1Bold,
            color = PrezelTheme.colors.textMedium,
        )
    }
}

@BasicPreview
@Composable
private fun GrowthGraphSectionPreview() {
    PrezelTheme {
        GrowthGraphSection(
            growthGraphData = ReportPreviewUpcomingUiState.growthGraphData,
            onCardIndexChange = {},
            onReRecordingClick = {},
        )
    }
}

@BasicPreview
@Composable
private fun EmptyGrowthGraphSectionPreview() {
    PrezelTheme {
        GrowthGraphSection(
            growthGraphData = GrowthGraphData(items = persistentListOf()),
            onCardIndexChange = {},
            onReRecordingClick = {},
        )
    }
}
