package com.team.prezel.core.designsystem.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun NestedCases(size: PrezelListSize) {
    Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12)) {
        Text("nested off", style = PrezelTheme.typography.body3Medium)
        BasePrezelList(size = size, nested = false)

        Text("nested on", style = PrezelTheme.typography.body3Medium)
        BasePrezelList(size = size, nested = true)
    }
}

@Composable
internal fun ShowLeadingCases(size: PrezelListSize) {
    Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12)) {
        Text("showLeadingContent off", style = PrezelTheme.typography.body3Medium)
        BasePrezelList(size = size, showLeading = false)

        Text("showLeadingContent on", style = PrezelTheme.typography.body3Medium)
        BasePrezelList(size = size, showLeading = true)
    }
}

@Composable
internal fun ShowTrailingCases(size: PrezelListSize) {
    Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12)) {
        Text("showTrailingContent off", style = PrezelTheme.typography.body3Medium)
        BasePrezelList(size = size, showTrailing = false)

        Text("showTrailingContent on", style = PrezelTheme.typography.body3Medium)
        BasePrezelList(size = size, showTrailing = true, showFirstTrailing = true)
    }
}

@Composable
internal fun ShowFirstTrailingCases(size: PrezelListSize) {
    Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12)) {
        Text("showFirstTrailingContent off", style = PrezelTheme.typography.body3Medium)
        BasePrezelList(size = size, showFirstTrailing = false)

        Text("showFirstTrailingContent on", style = PrezelTheme.typography.body3Medium)
        BasePrezelList(size = size, showFirstTrailing = true)
    }
}

@Composable
private fun prezelListTrailingIcons(size: PrezelListSize): ImmutableList<@Composable () -> Unit> =
    List(2) {
        @Composable {
            PrezelListIcon(
                icon = IconSource(resId = PrezelIcons.Blank),
                size = size,
            )
        }
    }.toPersistentList()

@Composable
internal fun BasePrezelList(
    size: PrezelListSize,
    nested: Boolean = false,
    showLeading: Boolean = true,
    showTrailing: Boolean = true,
    showFirstTrailing: Boolean = false,
) {
    PrezelList(
        title = "Title",
        size = size,
        nested = nested,
        showLeadingContent = showLeading,
        leadingContent = {
            PrezelListIcon(
                icon = IconSource(resId = PrezelIcons.Blank),
                size = size,
            )
        },
        showTrailingContent = showTrailing,
        showFirstTrailingContent = showFirstTrailing,
        trailingContents = prezelListTrailingIcons(size),
    )
}
