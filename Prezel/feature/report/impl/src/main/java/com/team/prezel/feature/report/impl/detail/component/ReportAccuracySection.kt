package com.team.prezel.feature.report.impl.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.feedback.tooltip.PrezelTooltipBox
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.practice.RecordingSpeed
import com.team.prezel.core.ui.component.graph.SpeedGraph
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.detail.model.SpeedGraphData
import com.team.prezel.feature.report.impl.detail.preview.ReportDetailPreviewData

@Composable
internal fun AccuracySection(
    accuracyScore: Double,
    scriptMatchRate: Double,
    speedGraphData: SpeedGraphData,
) {
    ReportSection(
        title = { AccuracySectionTitle() },
    ) {
        SpeedMetricRow(speedGraphData = speedGraphData)
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
        AccuracyMetricCards(
            accuracyScore = accuracyScore,
            scriptMatchRate = scriptMatchRate,
        )
    }
}

@Composable
private fun AccuracySectionTitle() {
    Text(
        text = stringResource(R.string.feature_report_impl_section_accuracy),
        style = PrezelTheme.typography.body2Bold,
        color = PrezelTheme.colors.textLarge,
    )
}

@Composable
private fun SpeedMetricRow(speedGraphData: SpeedGraphData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PrezelTheme.spacing.V6)
            .padding(start = PrezelTheme.spacing.V16, end = PrezelTheme.spacing.V20),
        verticalAlignment = Alignment.Bottom,
    ) {
        MetricLabel(
            title = stringResource(R.string.feature_report_impl_label_speed),
            label = speedGraphData.result.label(),
            labelColor = speedGraphData.result.labelColor(),
            modifier = Modifier.weight(1f),
        )
        SpeedGraph(userGauge = speedGraphData.spm)
    }
}

@Composable
private fun AccuracyMetricCards(
    accuracyScore: Double,
    scriptMatchRate: Double,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
    ) {
        MetricResultCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.feature_report_impl_label_speech),
            value = accuracyScore.toPercentLabel(),
        )
        MetricResultCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.feature_report_impl_label_script_match),
            value = scriptMatchRate.toPercentLabel(),
        )
    }
}

@Composable
private fun MetricLabel(
    title: String,
    label: String,
    labelColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V2),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = PrezelTheme.typography.body3Medium,
                color = PrezelTheme.colors.textMedium,
            )
            PrezelTooltipBox(
                text = "SPM은 1분당 말하는 음절의 수에요.",
            ) {
                Icon(
                    painter = painterResource(PrezelIcons.InfoCircleOutlined),
                    contentDescription = null,
                    tint = PrezelTheme.colors.iconRegular,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
        Text(
            text = label,
            style = PrezelTheme.typography.title1Bold,
            color = labelColor,
        )
    }
}

@Composable
private fun RecordingSpeed.label(): String =
    when (this) {
        RecordingSpeed.SLOW -> "느려요"
        RecordingSpeed.ADEQUATE -> "적당해요"
        RecordingSpeed.FAST -> "빨라요"
    }

@Composable
private fun RecordingSpeed.labelColor(): Color =
    when (this) {
        RecordingSpeed.ADEQUATE -> PrezelTheme.colors.feedbackGoodRegular
        RecordingSpeed.SLOW,
        RecordingSpeed.FAST,
        -> PrezelTheme.colors.feedbackWarningRegular
    }

@BasicPreview
@Composable
private fun AccuracySectionPreview() {
    PrezelTheme {
        AccuracySection(
            accuracyScore = ReportDetailPreviewData.reportDetail.accuracyScore,
            scriptMatchRate = ReportDetailPreviewData.reportDetail.scriptMatchRate,
            speedGraphData = ReportDetailPreviewData.reportDetail.speedGraphData,
        )
    }
}
