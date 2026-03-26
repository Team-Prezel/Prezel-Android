package com.team.prezel.core.designsystem.component.actions.button.config

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.designsystem.util.drawDashBorder

private data class ButtonPreviewGroup(
    val title: String,
    val enabled: Boolean,
    val isRounded: Boolean,
)

private val previewSections =
    listOf(
        ButtonPreviewGroup(title = "Enabled / Default", enabled = true, isRounded = false),
        ButtonPreviewGroup(title = "Enabled / Rounded", enabled = true, isRounded = true),
        ButtonPreviewGroup(title = "Disabled / Default", enabled = false, isRounded = false),
        ButtonPreviewGroup(title = "Disabled / Rounded", enabled = false, isRounded = true),
    )

@Composable
internal fun PrezelButtonPreviewContent(
    title: String,
    content: @Composable (ButtonType, ButtonHierarchy, ButtonSize, Boolean, Boolean) -> Unit,
) {
    PreviewSection(
        title = title,
        description = "행은 Size, 열은 Type과 Hierarchy 조합입니다. 각 섹션은 Enabled와 Rounded 상태를 구분합니다.",
    ) {
        previewSections.forEach { section ->
            ButtonPreviewSection(
                title = section.title,
                enabled = section.enabled,
                isRounded = section.isRounded,
                content = content,
            )
        }
    }
}

@Composable
internal fun Modifier.previewGhostBorder(
    type: ButtonType,
    hierarchy: ButtonHierarchy,
    size: ButtonSize,
    isRounded: Boolean,
    isIconOnly: Boolean,
): Modifier {
    if (type != ButtonType.GHOST) return this

    return this.drawDashBorder(
        shape = PrezelButtonDefaults
            .getDefault(
                isIconOnly = isIconOnly,
                isRounded = isRounded,
                type = type,
                size = size,
                hierarchy = hierarchy,
            ).shape,
    )
}

@Composable
private fun ButtonPreviewSection(
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
            ButtonPreviewHeaderRow()

            ButtonSize.entries.forEach { size ->
                ButtonPreviewRow(
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
private fun ButtonPreviewHeaderRow() {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        ButtonPreviewHeaderCell(
            text = "Size",
            modifier = Modifier.weight(1f),
        )

        ButtonType.entries.forEach { type ->
            ButtonHierarchy.entries.forEach { hierarchy ->
                ButtonPreviewHeaderCell(
                    text = "${type.name}\n${hierarchy.name}",
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun ButtonPreviewRow(
    size: ButtonSize,
    enabled: Boolean,
    isRounded: Boolean,
    content: @Composable (ButtonType, ButtonHierarchy, ButtonSize, Boolean, Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        ButtonPreviewCell(modifier = Modifier.weight(1f)) {
            Text(
                text = size.name,
                style = PrezelTheme.typography.body3Medium,
                color = PrezelTheme.colors.textLarge,
            )
        }

        ButtonType.entries.forEach { type ->
            ButtonHierarchy.entries.forEach { hierarchy ->
                ButtonPreviewCell(modifier = Modifier.weight(1f)) {
                    content(type, hierarchy, size, enabled, isRounded)
                }
            }
        }
    }
}

@Composable
private fun ButtonPreviewHeaderCell(
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
private fun ButtonPreviewCell(
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
