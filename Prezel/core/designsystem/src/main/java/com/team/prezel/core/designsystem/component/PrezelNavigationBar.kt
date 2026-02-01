package com.team.prezel.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = PrezelTheme.colors.bgRegular,
        tonalElevation = 0.dp,
        content = content,
    )
}

@Composable
fun RowScope.PrezelNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    @StringRes labelTextId: Int,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = if (selected) PrezelTheme.colors.iconMedium else PrezelTheme.colors.iconDisabled,
            )
        },
        modifier = modifier,
        enabled = enabled,
        label = {
            Text(
                text = stringResource(id = labelTextId),
                style = PrezelTheme.typography.caption2Medium,
            )
        },
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = PrezelTheme.colors.iconMedium,
            unselectedIconColor = PrezelTheme.colors.iconDisabled,
            selectedTextColor = PrezelTheme.colors.textLarge,
            unselectedTextColor = PrezelTheme.colors.textDisabled,
            indicatorColor = PrezelTheme.colors.bgRegular,
        ),
    )
}

@ThemePreview
@Composable
fun NiaNavigationBarPreview() {
    val items = listOf(
        Pair(PrezelIcons.Home, android.R.string.ok),
        Pair(PrezelIcons.Blank, android.R.string.copy),
        Pair(PrezelIcons.Profile, android.R.string.paste),
    )

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    PrezelTheme {
        PrezelNavigationBar {
            items.forEachIndexed { index, (iconRes, labelRes) ->
                PrezelNavigationBarItem(
                    selected = index == selectedIndex,
                    onClick = { selectedIndex = index },
                    iconRes = iconRes,
                    labelTextId = labelRes,
                )
            }
        }
    }
}
