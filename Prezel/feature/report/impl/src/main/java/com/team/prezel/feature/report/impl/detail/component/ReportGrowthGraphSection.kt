package com.team.prezel.feature.report.impl.detail.component

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
import com.team.prezel.feature.report.impl.detail.model.ImprovementGraphData
import com.team.prezel.feature.report.impl.detail.preview.ReportDetailPreviewData
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun GrowthGraphSection(
    improvementGraphData: ImprovementGraphData,
    onCardIndexChange: (Int) -> Unit,
) {
    val selectedItem = improvementGraphData.selectedItem ?: improvementGraphData.items.lastOrNull()

    ReportSection(
        title = { GrowthGraphSectionTitle() },
    ) {
        GrowthGraphContent(
            improvementGraphData = improvementGraphData,
            onCardIndexChange = onCardIndexChange,
        )
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
        GrowthGraphSummary(selectedItem = selectedItem)
    }
}

@Composable
private fun GrowthGraphSectionTitle() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row {
            Text(
                text = stringResource(R.string.feature_report_impl_section_growth_graph),
                style = PrezelTheme.typography.body2Bold,
                color = PrezelTheme.colors.textLarge,
            )
            Spacer(modifier = Modifier.width(PrezelTheme.spacing.V4))
            PrezelTooltipBox(
                text = "이전 연습과 비교하여 성장하는\n나의 발표를 확인할 수 있어요.",
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
            text = "다시 녹음하기",
            iconResId = PrezelIcons.Mic,
            type = ButtonType.OUTLINED,
            size = ButtonSize.XSMALL,
            hierarchy = ButtonHierarchy.SECONDARY,
            isRounded = true,
            onClick = {},
        )
    }
}

@Composable
private fun GrowthGraphContent(
    improvementGraphData: ImprovementGraphData,
    onCardIndexChange: (Int) -> Unit,
) {
    if (improvementGraphData.items.isEmpty()) {
        EmptyGrowthGraph()
        return
    }

    CardGraph(
        items = improvementGraphData.graphItems,
        selectedItemIndex = improvementGraphData.selectedItemIndex,
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
private fun GrowthGraphSummary(selectedItem: com.team.prezel.feature.report.impl.detail.model.ImprovementGraphItemUiModel?) {
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
            improvementGraphData = ReportDetailPreviewData.reportDetail.improvementGraphData,
            onCardIndexChange = {},
        )
    }
}

@BasicPreview
@Composable
private fun EmptyGrowthGraphSectionPreview() {
    PrezelTheme {
        GrowthGraphSection(
            improvementGraphData = ReportDetailPreviewData.reportDetail.improvementGraphData.copy(
                items = persistentListOf(),
            ),
            onCardIndexChange = {},
        )
    }
}
