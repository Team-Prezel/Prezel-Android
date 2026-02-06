package com.team.prezel.core.designsystem.component

import android.R
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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

@Composable
fun PrezelNavigationScaffold(
    navigationItems: @Composable PrezelNavigationScope.() -> Unit,
    modifier: Modifier = Modifier,
    showNavigationBar: Boolean = true,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (!showNavigationBar) return@Scaffold

            PrezelNavigationBar {
                PrezelNavigationScope(this).navigationItems()
            }
        },
        containerColor = PrezelTheme.colors.bgRegular,
        content = content,
    )
}

/**
 * 내비게이션 아이템을 선언하기 위한 스코프 wrapper입니다.
 * 앱 모듈의 호출 코드를 깔끔하고 일관되게 유지하기 위한 목적입니다.
 */
class PrezelNavigationScope internal constructor(
    private val rowScope: RowScope,
) {
    @Composable
    fun item(
        selected: Boolean,
        @StringRes labelTextId: Int,
        @DrawableRes iconRes: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        alwaysShowLabel: Boolean = true,
    ) {
        rowScope.PrezelNavigationBarItem(
            selected = selected,
            onClick = onClick,
            labelTextId = labelTextId,
            iconRes = iconRes,
            modifier = modifier,
            enabled = enabled,
            alwaysShowLabel = alwaysShowLabel,
        )
    }
}

@ThemePreview
@Composable
private fun PrezelNavigationScaffoldPreview() {
    PrezelTheme {
        PrezelNavigationScaffold(
            navigationItems = {
                item(
                    selected = true,
                    onClick = {},
                    labelTextId = R.string.untitled,
                    iconRes = PrezelIcons.Home,
                )
                item(
                    selected = false,
                    onClick = {},
                    labelTextId = R.string.copy,
                    iconRes = PrezelIcons.Storage,
                )
                item(
                    selected = false,
                    onClick = {},
                    labelTextId = R.string.paste,
                    iconRes = PrezelIcons.Profile,
                )
            },
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Text("Content")
            }
        }
    }
}

@ThemePreview
@Composable
fun NiaNavigationBarPreview() {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    val items = listOf(
        Triple(PrezelIcons.Home, R.string.untitled, 0),
        Triple(PrezelIcons.Storage, R.string.copy, 1),
        Triple(PrezelIcons.Profile, R.string.paste, 2),
    )

    PrezelTheme {
        PrezelNavigationScaffold(
            navigationItems = {
                items.forEach { (iconRes, labelRes, index) ->
                    item(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        labelTextId = labelRes,
                        iconRes = iconRes,
                    )
                }
            },
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Text("Selected: $selectedIndex")
            }
        }
    }
}
