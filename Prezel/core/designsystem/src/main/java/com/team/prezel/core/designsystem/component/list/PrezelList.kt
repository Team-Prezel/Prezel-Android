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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.SectionTitle
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelList(
    title: String,
    modifier: Modifier = Modifier,
    size: PrezelListSize = PrezelListSize.REGULAR,
    nested: Boolean = false,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(prezelListContentPadding(size, nested)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingContent?.let { content ->
            content()
            Spacer(modifier = Modifier.width(prezelListIconTextSpacing(size)))
        }

        PrezelListTitle(title, size)

        trailingContent?.let { content ->
            Spacer(modifier = Modifier.width(prezelListTextTrailingSpacing(size)))

            Row(
                horizontalArrangement = Arrangement.spacedBy(prezelListTrailingIconSpacing(size)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                content()
            }
        }
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
        maxLines = 1,
        style = prezelListTextStyle(size),
        color = LocalContentColor.current,
    )
}

@ThemePreview
@Composable
private fun PrezelListSmallPreview() {
    PrezelTheme {
        PreviewScaffold {
            SectionTitle("PrezelList - SMALL")
            PrezelListPreviewItem(size = PrezelListSize.SMALL, nested = false, showLeadingContent = true, showTrailingContent = true)
            PrezelListPreviewItem(size = PrezelListSize.SMALL, nested = false, showLeadingContent = true, showTrailingContent = false)
            PrezelListPreviewItem(size = PrezelListSize.SMALL, nested = false, showLeadingContent = false, showTrailingContent = true)
            PrezelListPreviewItem(size = PrezelListSize.SMALL, nested = false, showLeadingContent = false, showTrailingContent = false)
            PrezelListPreviewItem(size = PrezelListSize.SMALL, nested = true, showLeadingContent = true, showTrailingContent = true)
            PrezelListPreviewItem(size = PrezelListSize.SMALL, nested = true, showLeadingContent = true, showTrailingContent = false)
            PrezelListPreviewItem(size = PrezelListSize.SMALL, nested = true, showLeadingContent = false, showTrailingContent = true)
            PrezelListPreviewItem(size = PrezelListSize.SMALL, nested = true, showLeadingContent = false, showTrailingContent = false)
        }
    }
}

@ThemePreview
@Composable
private fun PrezelListRegularPreview() {
    PrezelTheme {
        PreviewScaffold {
            SectionTitle("PrezelList - REGULAR")
            PrezelListPreviewItem(size = PrezelListSize.REGULAR, nested = false, showLeadingContent = true, showTrailingContent = true)
            PrezelListPreviewItem(size = PrezelListSize.REGULAR, nested = false, showLeadingContent = true, showTrailingContent = false)
            PrezelListPreviewItem(size = PrezelListSize.REGULAR, nested = false, showLeadingContent = false, showTrailingContent = true)
            PrezelListPreviewItem(size = PrezelListSize.REGULAR, nested = false, showLeadingContent = false, showTrailingContent = false)
            PrezelListPreviewItem(size = PrezelListSize.REGULAR, nested = true, showLeadingContent = true, showTrailingContent = true)
            PrezelListPreviewItem(size = PrezelListSize.REGULAR, nested = true, showLeadingContent = true, showTrailingContent = false)
            PrezelListPreviewItem(size = PrezelListSize.REGULAR, nested = true, showLeadingContent = false, showTrailingContent = true)
            PrezelListPreviewItem(size = PrezelListSize.REGULAR, nested = true, showLeadingContent = false, showTrailingContent = false)
        }
    }
}

@Composable
private fun PrezelListPreviewItem(
    size: PrezelListSize,
    nested: Boolean,
    showLeadingContent: Boolean,
    showTrailingContent: Boolean,
    modifier: Modifier = Modifier,
) {
    val leadingContent: (@Composable RowScope.() -> Unit)? =
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

    val trailingContent: (@Composable RowScope.() -> Unit)? =
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
