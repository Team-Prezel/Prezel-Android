package com.team.prezel.core.designsystem.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelList(
    title: String,
    modifier: Modifier = Modifier,
    size: PrezelListSize = PrezelListSize.REGULAR,
    nested: Boolean = false,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(prezelListContentPadding(size, nested)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PrezelListLeadingSlot(size, leadingContent)
        PrezelListTitle(title, size)
        PrezelListTrailingSlot(size, trailingContent)
    }
}

@Composable
private fun PrezelListLeadingSlot(
    size: PrezelListSize,
    leadingContent: (@Composable () -> Unit)?,
) {
    leadingContent?.let {
        it()
        Spacer(modifier = Modifier.width(prezelListIconTextSpacing(size)))
    }
}

@Composable
private fun RowScope.PrezelListTitle(
    title: String,
    size: PrezelListSize,
) {
    Text(
        text = title,
        modifier = Modifier.weight(1f),
        color = LocalContentColor.current,
        maxLines = 1,
        style = prezelListTextStyle(size),
    )
}

@Composable
private fun PrezelListTrailingSlot(
    size: PrezelListSize,
    trailingContent: (@Composable () -> Unit)?,
) {
    trailingContent?.let {
        Spacer(modifier = Modifier.width(prezelListTextTrailingSpacing(size)))
        Row(
            horizontalArrangement = Arrangement.spacedBy(prezelListTrailingIconSpacing(size)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            it()
        }
    }
}

@Composable
private fun PrezelListSizeCases(size: PrezelListSize) {
    Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V24)) {
        NestedCases(size)
        ShowLeadingCases(size)
        ShowTrailingCases(size)
        ShowFirstTrailingCases(size)
    }
}

@ThemePreview
@Composable
private fun PrezelListSmallPreview() {
    PrezelTheme {
        PreviewScaffold {
            Text(text = "PrezelList - SMALL", style = PrezelTheme.typography.title2Medium)
            HorizontalDivider()
            PrezelListSizeCases(PrezelListSize.SMALL)
        }
    }
}

@ThemePreview
@Composable
private fun PrezelListRegularPreview() {
    PrezelTheme {
        PreviewScaffold {
            Text(text = "PrezelList - REGULAR", style = PrezelTheme.typography.title2Medium)
            HorizontalDivider()
            PrezelListSizeCases(PrezelListSize.REGULAR)
        }
    }
}
