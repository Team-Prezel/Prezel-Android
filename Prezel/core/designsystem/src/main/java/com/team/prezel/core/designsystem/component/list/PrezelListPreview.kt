package com.team.prezel.core.designsystem.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme

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
        BasePrezelList(size = size)

        Text("showLeadingContent on", style = PrezelTheme.typography.body3Medium)
        BasePrezelList(size = size)
    }
}

@Composable
internal fun ShowTrailingCases(size: PrezelListSize) {
    Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12)) {
        Text("showTrailingContent off", style = PrezelTheme.typography.body3Medium)
        BasePrezelList(size = size)

        Text("showTrailingContent on", style = PrezelTheme.typography.body3Medium)
        BasePrezelList(size = size)
    }
}

@Composable
internal fun ShowFirstTrailingCases(size: PrezelListSize) {
    Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12)) {
        Text("showFirstTrailingContent off", style = PrezelTheme.typography.body3Medium)
        BasePrezelList(size = size)

        Text("showFirstTrailingContent on", style = PrezelTheme.typography.body3Medium)
        BasePrezelList(size = size)
    }
}

@Composable
internal fun BasePrezelList(
    size: PrezelListSize,
    nested: Boolean = false,
) {
    PrezelList(
        title = "Title",
        size = size,
        nested = nested,
        leadingContent = {
            PrezelListIcon(
                icon = IconSource(resId = PrezelIcons.Blank),
                size = size,
            )
        },
        trailingContent = {
            PrezelListIcon(
                icon = IconSource(resId = PrezelIcons.Blank),
                size = size,
            )
            PrezelListIcon(
                icon = IconSource(resId = PrezelIcons.Blank),
                size = size,
            )
        },
    )
}
