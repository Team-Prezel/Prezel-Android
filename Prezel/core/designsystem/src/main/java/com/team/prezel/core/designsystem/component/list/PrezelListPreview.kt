package com.team.prezel.core.designsystem.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.persistentListOf

@Immutable
private data class PrezelListVariant(
    val hasLeading: Boolean,
    val hasTrailing: Boolean,
)

private val PrezelListVariants = persistentListOf(
    PrezelListVariant(hasLeading = false, hasTrailing = false),
    PrezelListVariant(hasLeading = true, hasTrailing = false),
    PrezelListVariant(hasLeading = false, hasTrailing = true),
    PrezelListVariant(hasLeading = true, hasTrailing = true),
)

@Composable
internal fun PrezelListPreviewBySize(
    size: PrezelListSize,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12),
    ) {
        PrezelListVariants.forEach { variant ->
            HorizontalDivider()
            PrezelListVariantSection(
                size = size,
                nested = false,
                variant = variant,
            )
        }

        HorizontalDivider()
        PrezelListVariantSection(
            size = size,
            nested = true,
            variant = PrezelListVariant(hasLeading = true, hasTrailing = true),
        )
    }
}

@Composable
private fun PrezelListVariantSection(
    size: PrezelListSize,
    nested: Boolean,
    variant: PrezelListVariant,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12),
    ) {
        Text(
            text = "Nested: $nested | Leading: ${variant.hasLeading} | Trailing: ${variant.hasTrailing}",
            style = PrezelTheme.typography.body3Medium,
        )

        PrezelListCaseRow(size = size, nested = nested, variant = variant)
    }
}

@Composable
private fun PrezelListCaseRow(
    size: PrezelListSize,
    nested: Boolean,
    variant: PrezelListVariant,
) {
    val leading: (@Composable () -> Unit)? =
        if (variant.hasLeading) {
            {
                Icon(
                    painter = painterResource(id = PrezelIcons.Blank),
                    contentDescription = "leading",
                )
            }
        } else {
            null
        }

    val trailing: (@Composable () -> Unit)? =
        if (variant.hasTrailing) {
            {
                Icon(
                    painter = painterResource(id = PrezelIcons.Blank),
                    contentDescription = "trailing",
                )
            }
        } else {
            null
        }

    PrezelList(
        title = "Title",
        size = size,
        nested = nested,
        leadingContent = leading,
        trailingContent = trailing,
    )
}
