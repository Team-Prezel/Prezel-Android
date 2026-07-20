package com.team.prezel.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.feedback.snackbar.PrezelSnackbarHost
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
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

        Row(
            modifier = Modifier
                .background(PrezelTheme.colors.bgRegular)
                .padding(horizontal = PrezelTheme.spacing.V20)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
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
) {
    Column(
        modifier = modifier
            .weight(1f)
            .clickable(
                enabled = enabled,
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))
        Icon(
            modifier = Modifier.size(28.dp),
            painter = painterResource(id = iconResId),
            tint = if (selected) PrezelTheme.colors.iconMedium else PrezelTheme.colors.iconDisabled,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V4))
        Text(
            text = label,
            style = PrezelTheme.typography.caption2Medium,
            color = if (selected) PrezelTheme.colors.textLarge else PrezelTheme.colors.textDisabled,
        )
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))
    }
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
            AnimatedVisibility(
                visible = showNavigationBar,
                exit = ExitTransition.None,
                enter = EnterTransition.None,
            ) {
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
    ) {
        rowScope.PrezelNavigationBarItem(
            selected = selected,
            onClick = onClick,
            iconResId = iconResId,
            label = label,
            modifier = modifier,
            enabled = enabled,
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
