package com.team.prezel.feature.report.impl.script.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelDividerType
import com.team.prezel.core.designsystem.component.PrezelHorizontalDivider
import com.team.prezel.core.designsystem.component.PrezelVerticalDivider
import com.team.prezel.core.designsystem.component.chip.chip.ChipAccent
import com.team.prezel.core.designsystem.component.chip.chip.ChipHierarchy
import com.team.prezel.core.designsystem.component.chip.chip.ChipSize
import com.team.prezel.core.designsystem.component.chip.chip.PrezelChip
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.R

@Composable
internal fun ScriptResultSummary(
    spellingCount: Int,
    grammarCount: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        PrezelHorizontalDivider(
            color = PrezelTheme.colors.borderSmall,
            type = PrezelDividerType.THICK,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PrezelTheme.spacing.V20)
                .padding(top = PrezelTheme.spacing.V16, bottom = PrezelTheme.spacing.V8),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
        ) {
            SummaryItem(
                label = stringResource(R.string.feature_report_impl_script_spelling),
                count = spellingCount,
                accent = ChipAccent.PURPLE,
                modifier = Modifier.weight(1f),
            )

            PrezelVerticalDivider(
                color = PrezelTheme.colors.borderSmall,
                type = PrezelDividerType.THICK,
                modifier = Modifier.height(24.dp),
            )

            SummaryItem(
                label = stringResource(R.string.feature_report_impl_script_grammar),
                count = grammarCount,
                accent = ChipAccent.TEAL,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SummaryItem(
    label: String,
    count: Int,
    accent: ChipAccent,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PrezelChip(
            text = label,
            size = ChipSize.SMALL,
            hierarchy = ChipHierarchy.PRIMARY,
            accent = accent,
        )

        Text(
            text = stringResource(R.string.feature_report_impl_script_count, count),
            style = PrezelTheme.typography.body2Medium,
            color = PrezelTheme.colors.textLarge,
        )
    }
}

@BasicPreview
@Composable
private fun ScriptResultSummaryPreview() {
    PrezelTheme {
        Box(
            modifier = Modifier
                .background(PrezelTheme.colors.bgMedium)
                .padding(8.dp),
        ) {
            ScriptResultSummary(
                spellingCount = 3,
                grammarCount = 5,
            )
        }
    }
}
