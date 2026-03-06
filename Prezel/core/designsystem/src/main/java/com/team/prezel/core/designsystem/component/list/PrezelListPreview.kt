package com.team.prezel.core.designsystem.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun PrezelListPreviewItem(
    size: PrezelListSize,
    nested: Boolean,
    showLeadingContent: Boolean,
    showTrailingContent: Boolean,
    modifier: Modifier = Modifier,
) {
    val leadingContent: (@Composable () -> Unit)? =
        if (showLeadingContent) {
            {
                Icon(
                    painter = painterResource(id = PrezelIcons.Blank),
                    contentDescription = "leading",
                )
            }
        } else {
            null
        }

    val trailingContent: (@Composable () -> Unit)? =
        if (showTrailingContent) {
            {
                Icon(
                    painter = painterResource(id = PrezelIcons.Blank),
                    contentDescription = "trailing",
                )
            }
        } else {
            null
        }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12),
    ) {
        Text(
            text = "Nested: $nested | Leading: $showLeadingContent | Trailing: $showTrailingContent",
            style = PrezelTheme.typography.body3Medium,
        )

        PrezelList(
            title = "Title",
            size = size,
            nested = nested,
            leadingContent = leadingContent,
            trailingContent = trailingContent,
        )

        HorizontalDivider()
    }
}

@Composable
internal fun PrezelListPreviewBySize(
    size: PrezelListSize,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12),
    ) {
        PrezelListPreviewItem(size, nested = false, showLeadingContent = true, showTrailingContent = true)
        PrezelListPreviewItem(size, nested = false, showLeadingContent = true, showTrailingContent = false)
        PrezelListPreviewItem(size, nested = false, showLeadingContent = false, showTrailingContent = true)
        PrezelListPreviewItem(size, nested = false, showLeadingContent = false, showTrailingContent = false)

        PrezelListPreviewItem(size, nested = true, showLeadingContent = true, showTrailingContent = true)
        PrezelListPreviewItem(size, nested = true, showLeadingContent = true, showTrailingContent = false)
        PrezelListPreviewItem(size, nested = true, showLeadingContent = false, showTrailingContent = true)
        PrezelListPreviewItem(size, nested = true, showLeadingContent = false, showTrailingContent = false)
    }
}
