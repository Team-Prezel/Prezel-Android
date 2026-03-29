package com.team.prezel.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.snackbar.PrezelSnackbarHost
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.designsystem.util.NoRippleInteractionSource

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
    @DrawableRes iconResId: Int,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
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
            indicatorColor = Color.Transparent,
        ),
        interactionSource = NoRippleInteractionSource,
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
            AnimatedVisibility(visible = showNavigationBar) {
                PrezelNavigationBar {
                    PrezelNavigationScope(this).navigationItems()
                }
            }
        },
        snackbarHost = {
            PrezelSnackbarHost(hostState = snackbarHostState)
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
    fun Item(
        selected: Boolean,
        @DrawableRes iconResId: Int,
        label: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        alwaysShowLabel: Boolean = true,
    ) {
        rowScope.PrezelNavigationBarItem(
            selected = selected,
            onClick = onClick,
            iconResId = iconResId,
            label = label,
            modifier = modifier,
            enabled = enabled,
            alwaysShowLabel = alwaysShowLabel,
        )
    }
}

@BasicPreview
@Composable
private fun PrezelNavigationScaffoldPreview() {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    PrezelTheme {
        PrezelNavigationScaffold(
            snackbarHostState = snackbarHostState,
            navigationItems = {
                repeat(3) { index ->
                    Item(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        iconResId = PrezelIcons.Blank,
                        label = "Tab $index",
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
