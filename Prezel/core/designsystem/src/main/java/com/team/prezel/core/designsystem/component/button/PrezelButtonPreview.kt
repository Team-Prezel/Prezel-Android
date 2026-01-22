package com.team.prezel.core.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.persistentListOf

internal typealias PrezelButtonPreviewContent = @Composable (PrezelButtonStyle, Boolean) -> Unit

@Immutable
private data class PreviewVariant(
    val enabled: Boolean,
    val isRounded: Boolean,
)

private val PreviewVariants = persistentListOf(
    PreviewVariant(enabled = true, isRounded = false),
    PreviewVariant(enabled = true, isRounded = true),
    PreviewVariant(enabled = false, isRounded = true),
    PreviewVariant(enabled = false, isRounded = false),
)

private val PreviewSizes = persistentListOf(
    PrezelButtonSize.XSMALL,
    PrezelButtonSize.SMALL,
    PrezelButtonSize.REGULAR,
)

@Composable
internal fun PrezelButtonPreviewByType(
    type: PrezelButtonType,
    content: PrezelButtonPreviewContent,
) {
    PreviewScaffold {
        Text(text = type.name, style = PrezelTheme.typography.title2Medium)

        PreviewVariants.forEach { variant ->
            HorizontalDivider()
            PrezelButtonVariantSection(
                type = type,
                enabled = variant.enabled,
                isRounded = variant.isRounded,
                content = content,
            )
        }
    }
}

@Composable
private fun PrezelButtonVariantSection(
    type: PrezelButtonType,
    enabled: Boolean,
    isRounded: Boolean,
    content: PrezelButtonPreviewContent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = "Hierarchy: Primary | Enabled: $enabled | Radius: $isRounded", style = PrezelTheme.typography.body3Medium)
        PrezelButtonPreviewHierarchyBlock(
            type = type,
            hierarchy = PrezelButtonHierarchy.PRIMARY,
            enabled = enabled,
            isRounded = isRounded,
            content = content,
        )
        Text(text = "Hierarchy: Secondary | Enabled: $enabled | Radius: $isRounded", style = PrezelTheme.typography.body3Medium)
        PrezelButtonPreviewHierarchyBlock(
            type = type,
            hierarchy = PrezelButtonHierarchy.SECONDARY,
            enabled = enabled,
            isRounded = isRounded,
            content = content,
        )
    }
}

@Composable
private fun PrezelButtonPreviewHierarchyBlock(
    type: PrezelButtonType,
    hierarchy: PrezelButtonHierarchy,
    enabled: Boolean,
    isRounded: Boolean,
    content: PrezelButtonPreviewContent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PreviewSizes.forEach { size ->
                content(
                    PrezelButtonStyle(
                        buttonType = type,
                        buttonHierarchy = hierarchy,
                        buttonSize = size,
                        isRounded = isRounded,
                    ),
                    enabled,
                )
            }
        }
    }
}
