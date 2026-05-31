package com.team.prezel.feature.report.impl.report.component.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.PrezelTextButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.graph.StickGraph
import com.team.prezel.core.ui.component.graph.StickGraphItemType
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.report.component.common.EmptyStateCard
import com.team.prezel.feature.report.impl.report.component.common.MetricResultCard
import com.team.prezel.feature.report.impl.report.component.common.ReportSection
import com.team.prezel.feature.report.impl.report.model.ScriptAnalysisGraphData
import com.team.prezel.feature.report.impl.report.preview.ReportPreviewUpcomingUiState
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentMap

@Composable
internal fun ScriptAnalysisSection(
    isWrittenScript: Boolean,
    scriptAnalysisGraphData: ScriptAnalysisGraphData,
    onReWriteScriptClick: () -> Unit,
) {
    ReportSection(
        title = {
            ScriptAnalysisSectionTitle(
                showReWriteButton = isWrittenScript,
                onReWriteScriptClick = onReWriteScriptClick,
            )
        },
    ) {
        if (isWrittenScript) {
            ScriptAnalysisContent(scriptAnalysisGraphData = scriptAnalysisGraphData)
            return@ReportSection
        }

        EmptyStateContent(onReWriteScriptClick = onReWriteScriptClick)
    }
}

@Composable
private fun EmptyStateContent(onReWriteScriptClick: () -> Unit) {
    EmptyStateCard(text = stringResource(R.string.feature_report_impl_script_analysis_empty_state_message))

    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))

    PrezelTextButton(
        text = stringResource(R.string.feature_report_impl_write_script),
        size = ButtonSize.REGULAR,
        type = ButtonType.FILLED,
        hierarchy = ButtonHierarchy.SECONDARY,
        onClick = onReWriteScriptClick,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ScriptAnalysisSectionTitle(
    showReWriteButton: Boolean,
    onReWriteScriptClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.feature_report_impl_section_script_analysis),
            style = PrezelTheme.typography.title2Bold,
            color = PrezelTheme.colors.textLarge,
        )

        if (showReWriteButton) {
            PrezelButton(
                text = stringResource(R.string.feature_report_impl_re_write_script),
                iconResId = PrezelIcons.Script,
                type = ButtonType.OUTLINED,
                size = ButtonSize.XSMALL,
                hierarchy = ButtonHierarchy.SECONDARY,
                isRounded = true,
                onClick = onReWriteScriptClick,
            )
        }
    }
}

@Composable
private fun ScriptAnalysisContent(scriptAnalysisGraphData: ScriptAnalysisGraphData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
        verticalAlignment = Alignment.Bottom,
    ) {
        ScriptAnalysisGraph(graphData = scriptAnalysisGraphData.toStickGraphData())
        TotalErrorCard(totalErrorCount = scriptAnalysisGraphData.totalErrorCount)
    }
}

@Composable
private fun RowScope.ScriptAnalysisGraph(graphData: ImmutableMap<StickGraphItemType, Int>) {
    Box(
        modifier = Modifier.weight(1f),
        contentAlignment = Alignment.BottomCenter,
    ) {
        StickGraph(data = graphData)
    }
}

@Composable
private fun RowScope.TotalErrorCard(totalErrorCount: Int) {
    Box(modifier = Modifier.weight(1f)) {
        MetricResultCard(
            title = stringResource(R.string.feature_report_impl_label_total_errors),
            value = totalErrorCount.toString(),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private fun ScriptAnalysisGraphData.toStickGraphData() =
    StickGraphItemType.entries
        .associateWith { type ->
            when (type) {
                StickGraphItemType.SPELLING -> spellingCount
                StickGraphItemType.GRAMMAR -> grammarCount
            }
        }.toPersistentMap()

@BasicPreview
@Composable
private fun ScriptAnalysisSectionPreview() {
    PrezelTheme {
        ScriptAnalysisSection(
            isWrittenScript = true,
            scriptAnalysisGraphData = ReportPreviewUpcomingUiState.scriptAnalysisGraphData,
            onReWriteScriptClick = {},
        )
    }
}

@BasicPreview
@Composable
private fun EmptyScriptAnalysisSectionPreview() {
    PrezelTheme {
        ScriptAnalysisSection(
            isWrittenScript = false,
            scriptAnalysisGraphData = ReportPreviewUpcomingUiState.scriptAnalysisGraphData,
            onReWriteScriptClick = {},
        )
    }
}
