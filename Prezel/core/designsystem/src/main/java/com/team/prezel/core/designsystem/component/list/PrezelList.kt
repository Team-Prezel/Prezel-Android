package com.team.prezel.core.designsystem.component.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.SectionTitle
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.designsystem.util.drawDashBorder

@Composable
fun PrezelList(
    title: String,
    modifier: Modifier = Modifier,
    size: PrezelListSize = PrezelListSize.REGULAR,
    nested: Boolean = false,
    leadingContent: @Composable (RowScope.() -> Unit)? = null,
    trailingContent: @Composable (RowScope.() -> Unit)? = null,
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
            PrezelListPreviewBySize(PrezelListSize.SMALL)
        }
    }
}

@ThemePreview
@Composable
private fun PrezelListRegularPreview() {
    PrezelTheme {
        PreviewScaffold {
            PrezelListPreviewBySize(PrezelListSize.REGULAR)
        }
    }
}

@Composable
private fun PrezelListPreviewItem(
    size: PrezelListSize,
    nested: Boolean,
    showLeadingContent: Boolean,
    showTrailingContent: Boolean,
) {
    val leadingContent: @Composable RowScope.() -> Unit = {
        Icon(
            painter = painterResource(id = PrezelIcons.Blank),
            contentDescription = "leading",
        )
    }

    val trailingContent: @Composable RowScope.() -> Unit = {
        Icon(
            painter = painterResource(id = PrezelIcons.Blank),
            contentDescription = "trailing",
        )
        Icon(
            painter = painterResource(id = PrezelIcons.Blank),
            contentDescription = "trailing",
        )
    }

    PrezelList(
        title = "Title",
        size = size,
        nested = nested,
        leadingContent = if (showLeadingContent) leadingContent else null,
        trailingContent = if (showTrailingContent) trailingContent else null,
        modifier = Modifier.drawDashBorder(),
    )
}

@Composable
private fun PrezelListPreviewBySize(size: PrezelListSize) {
    SectionTitle(title = "PrezelList - $size")
    Text(
        text = "점선 테두리는 컴포넌트 경계를 의미하며,\nnested 상태별 leading/trailing 조합을 확인할 수 있습니다.",
        style = PrezelTheme.typography.body3Regular,
        color = PrezelTheme.colors.textMedium,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.5f))
            .padding(8.dp),
    )

    Text(text = "Nested: False", style = PrezelTheme.typography.body2Bold)
    PrezelListPreviewItem(size, nested = false, showLeadingContent = true, showTrailingContent = true)
    PrezelListPreviewItem(size, nested = false, showLeadingContent = true, showTrailingContent = false)
    PrezelListPreviewItem(size, nested = false, showLeadingContent = false, showTrailingContent = true)
    PrezelListPreviewItem(size, nested = false, showLeadingContent = false, showTrailingContent = false)

    Spacer(modifier = Modifier.height(8.dp))
    Text(text = "Nested: True", style = PrezelTheme.typography.body2Bold)
    PrezelListPreviewItem(size, nested = true, showLeadingContent = true, showTrailingContent = true)
    PrezelListPreviewItem(size, nested = true, showLeadingContent = true, showTrailingContent = false)
    PrezelListPreviewItem(size, nested = true, showLeadingContent = false, showTrailingContent = true)
    PrezelListPreviewItem(size, nested = true, showLeadingContent = false, showTrailingContent = false)
}
