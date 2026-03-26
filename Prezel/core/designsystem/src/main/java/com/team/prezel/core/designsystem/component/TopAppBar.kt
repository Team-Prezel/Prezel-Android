package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrezelTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    leadingIcon: @Composable () -> Unit = {},
    trailingIcons: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title = {
            ProvideTextStyle(PrezelTheme.typography.body2Bold) {
                title()
            }
        },
        navigationIcon = leadingIcon,
        actions = trailingIcons,
        colors = prezelTopAppBarColors(),
        scrollBehavior = scrollBehavior,
        modifier = modifier.testTag("PrezelTopAppBar"),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun prezelTopAppBarColors() =
    TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = PrezelTheme.colors.bgRegular,
        navigationIconContentColor = PrezelTheme.colors.iconRegular,
        titleContentColor = PrezelTheme.colors.textLarge,
        actionIconContentColor = PrezelTheme.colors.iconRegular,
    )

@OptIn(ExperimentalMaterial3Api::class)
@BasicPreview
@Composable
private fun PrezelTopAppBarTitleOnlyPreview() {
    PreviewSection(title = "Title Only") {
        PrezelTopAppBar(title = { Text(text = "제목") })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@BasicPreview
@Composable
private fun PrezelTopAppBarWithLeadingPreview() {
    PreviewSection(title = "With Leading") {
        PrezelTopAppBar(
            title = { Text(text = "제목") },
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(PrezelIcons.Blank),
                        contentDescription = "뒤로가기",
                    )
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@BasicPreview
@Composable
private fun PrezelTopAppBarWithAllIconsPreview() {
    PreviewSection(title = "With All Icons") {
        PrezelTopAppBar(
            title = { Text(text = "Title") },
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(PrezelIcons.Blank),
                        contentDescription = "뒤로가기",
                    )
                }
            },
            trailingIcons = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(PrezelIcons.Blank),
                        contentDescription = "검색",
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(PrezelIcons.Blank),
                        contentDescription = "메뉴",
                    )
                }
            },
        )
    }
}
