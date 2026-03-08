package com.team.prezel.core.designsystem.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    )
}

@ThemePreview
@Composable
private fun PrezelListSmallPreview() {
    PrezelTheme {
        PreviewScaffold {
            SectionTitle("PrezelList - SMALL")
            PrezelListPreviewBySize(PrezelListSize.SMALL)
        }
    }
}

@ThemePreview
@Composable
private fun PrezelListRegularPreview() {
    PrezelTheme {
        PreviewScaffold {
            SectionTitle("PrezelList - REGULAR")
            PrezelListPreviewBySize(PrezelListSize.REGULAR)
        }
    }
}
