package com.team.prezel.core.designsystem.component

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.DrawableIcon
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(PrezelTheme.colors.borderRegular),
        )

        NavigationBar(
            containerColor = PrezelTheme.colors.bgRegular,
            tonalElevation = 0.dp,
            content = content,
        )
    }
}

@Composable
fun RowScope.PrezelNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    icon: IconSource,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                icon.painter(),
                contentDescription = icon.contentDescription(),
            )
        },
        modifier = modifier,
        enabled = enabled,
        label = {
            Text(
                text = label,
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
    snackbarHostState: SnackbarHostState,
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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
        label: String,
        icon: IconSource,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        alwaysShowLabel: Boolean = true,
    ) {
        rowScope.PrezelNavigationBarItem(
            selected = selected,
            onClick = onClick,
            label = label,
            icon = icon,
            modifier = modifier,
            enabled = enabled,
            alwaysShowLabel = alwaysShowLabel,
        )
    }
}

@ThemePreview
@Composable
private fun PrezelNavigationScaffoldPreview() {
    val snackbarHostState = remember { SnackbarHostState() }

    PrezelTheme {
        PrezelNavigationScaffold(
            snackbarHostState = snackbarHostState,
            navigationItems = {
                item(
                    selected = true,
                    onClick = {},
                    label = stringResource(R.string.untitled),
                    icon = DrawableIcon(
                        resId = PrezelIcons.Home,
                        contentDescTextId = R.string.untitled,
                    ),
                )
                item(
                    selected = false,
                    onClick = {},
                    label = stringResource(R.string.copy),
                    icon = DrawableIcon(
                        resId = PrezelIcons.Storage,
                        contentDescTextId = R.string.copy,
                    ),
                )
                item(
                    selected = false,
                    onClick = {},
                    label = stringResource(R.string.paste),
                    icon = DrawableIcon(
                        resId = PrezelIcons.Profile,
                        contentDescTextId = R.string.paste,
                    ),
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
private fun PrezelNavigationBarPreview() {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    val items = listOf(
        Triple(DrawableIcon(PrezelIcons.Home), stringResource(R.string.untitled), 0),
        Triple(DrawableIcon(PrezelIcons.Storage), stringResource(R.string.copy), 1),
        Triple(DrawableIcon(PrezelIcons.Profile), stringResource(R.string.paste), 2),
    )

    PrezelTheme {
        PrezelNavigationScaffold(
            snackbarHostState = snackbarHostState,
            navigationItems = {
                items.forEach { (icon, label, index) ->
                    item(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        label = label,
                        icon = icon,
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
