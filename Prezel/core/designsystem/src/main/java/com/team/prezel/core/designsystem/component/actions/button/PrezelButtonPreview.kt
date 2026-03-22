package com.team.prezel.core.designsystem.component.actions.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.SectionTitle
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.designsystem.util.drawDashBorder

private data class PreviewSection(
    val title: String,
    val enabled: Boolean,
    val isRounded: Boolean,
)

private val previewSections =
    listOf(
        PreviewSection(title = "Enabled / Default", enabled = true, isRounded = false),
        PreviewSection(title = "Enabled / Rounded", enabled = true, isRounded = true),
        PreviewSection(title = "Disabled / Default", enabled = false, isRounded = false),
        PreviewSection(title = "Disabled / Rounded", enabled = false, isRounded = true),
    )

@Preview(device = "spec:width=1080dp,height=1400dp")
@Composable
private fun PrezelButtonsPreview() {
    PrezelButtonPreviewScreen(title = "Button/Icon + Text") { type, hierarchy, size, enabled, isRounded ->
        PrezelButton(
            text = "Label",
            iconResId = PrezelIcons.Blank,
            type = type,
            size = size,
            hierarchy = hierarchy,
            enabled = enabled,
            isRounded = isRounded,
            onClick = {},
            modifier = Modifier.previewGhostBorderModifier(
                type = type,
                hierarchy = hierarchy,
                size = size,
                enabled = enabled,
                isRounded = isRounded,
                isIconOnly = false,
            ),
        )
    }
}

@Preview(device = "spec:width=1080dp,height=1350dp")
@Composable
private fun PrezelTextButtonsPreview() {
    PrezelButtonPreviewScreen(title = "Button/Text") { type, hierarchy, size, enabled, isRounded ->
        PrezelTextButton(
            text = "Label",
            type = type,
            size = size,
            hierarchy = hierarchy,
            enabled = enabled,
            isRounded = isRounded,
            onClick = {},
            modifier = Modifier.previewGhostBorderModifier(
                type = type,
                hierarchy = hierarchy,
                size = size,
                enabled = enabled,
                isRounded = isRounded,
                isIconOnly = false,
            ),
        )
    }
}

@Preview(device = "spec:width=1080dp,height=1500dp")
@Composable
private fun PrezelIconButtonsPreview() {
    PrezelButtonPreviewScreen(title = "Button/Icon") { type, hierarchy, size, enabled, isRounded ->
        PrezelIconButton(
            iconResId = PrezelIcons.Blank,
            type = type,
            size = size,
            hierarchy = hierarchy,
            enabled = enabled,
            isRounded = isRounded,
            onClick = {},
            modifier = Modifier.previewGhostBorderModifier(
                type = type,
                hierarchy = hierarchy,
                size = size,
                enabled = enabled,
                isRounded = isRounded,
                isIconOnly = true,
            ),
        )
    }
}

@Composable
private fun PrezelButtonPreviewScreen(
    title: String,
    content: @Composable (ButtonType, ButtonHierarchy, ButtonSize, Boolean, Boolean) -> Unit,
) {
    PrezelTheme {
        PreviewScaffold {
            SectionTitle(title = title)
            Text(
                text = "행은 Size, 열은 Type과 Hierarchy 조합입니다. 각 섹션은 Enabled와 Rounded 상태를 구분합니다.",
                style = PrezelTheme.typography.body3Regular,
                color = PrezelTheme.colors.textMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(alpha = 0.4f))
                    .padding(8.dp),
            )

            previewSections.forEach { section ->
                PrezelButtonPreviewTable(
                    title = section.title,
                    enabled = section.enabled,
                    isRounded = section.isRounded,
                    content = content,
                )
            }
        }
    }
}

@Composable
private fun PrezelButtonPreviewTable(
    title: String,
    enabled: Boolean,
    isRounded: Boolean,
    content: @Composable (ButtonType, ButtonHierarchy, ButtonSize, Boolean, Boolean) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = PrezelTheme.typography.body3Bold,
            color = PrezelTheme.colors.textLarge,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            PrezelButtonTableHeader()

            ButtonSize.entries.forEach { size ->
                PrezelButtonTableRow(
                    size = size,
                    enabled = enabled,
                    isRounded = isRounded,
                    content = content,
                )
            }
        }
    }
}

@Composable
private fun PrezelButtonTableHeader() {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        PreviewHeaderCell(
            text = "Size",
            modifier = Modifier.defaultMinSize(minWidth = 88.dp),
        )

        ButtonType.entries.forEach { type ->
            ButtonHierarchy.entries.forEach { hierarchy ->
                PreviewHeaderCell(
                    text = "${type.name}\n${hierarchy.name}",
                    modifier = Modifier.defaultMinSize(minWidth = 156.dp),
                )
            }
        }
    }
}

@Composable
private fun PrezelButtonTableRow(
    size: ButtonSize,
    enabled: Boolean,
    isRounded: Boolean,
    content: @Composable (ButtonType, ButtonHierarchy, ButtonSize, Boolean, Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        PreviewButtonCell(modifier = Modifier.defaultMinSize(minWidth = 88.dp)) {
            Text(
                text = size.name.lowercase(),
                style = PrezelTheme.typography.body3Medium,
                color = PrezelTheme.colors.textLarge,
            )
        }

        ButtonType.entries.forEach { type ->
            ButtonHierarchy.entries.forEach { hierarchy ->
                PreviewButtonCell(modifier = Modifier.defaultMinSize(minWidth = 156.dp)) {
                    content(type, hierarchy, size, enabled, isRounded)
                }
            }
        }
    }
}

@Composable
private fun PreviewHeaderCell(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .border(width = PrezelTheme.stroke.V1, color = PrezelTheme.colors.borderRegular)
            .background(PrezelTheme.colors.bgMedium)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = PrezelTheme.typography.body3Medium,
            color = PrezelTheme.colors.textMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PreviewButtonCell(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .border(width = PrezelTheme.stroke.V1, color = PrezelTheme.colors.borderRegular)
            .padding(horizontal = 12.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
private fun Modifier.previewGhostBorderModifier(
    type: ButtonType,
    hierarchy: ButtonHierarchy,
    size: ButtonSize,
    enabled: Boolean,
    isRounded: Boolean,
    isIconOnly: Boolean,
): Modifier =
    if (type == ButtonType.GHOST) {
        this.drawDashBorder(
            shape = PrezelButtonDefaults
                .getDefault(
                    isIconOnly = isIconOnly,
                    isRounded = isRounded,
                    type = type,
                    size = size,
                    hierarchy = hierarchy,
                    enabled = enabled,
                ).shape,
        )
    } else {
        Modifier
    }
