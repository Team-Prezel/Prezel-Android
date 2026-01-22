package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
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
            ProvideTextStyle(PrezelTextStyles.Body2Bold.toTextStyle()) {
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
@ThemePreview
@Composable
private fun PrezelTopAppBarTitleOnlyPreview() {
    PrezelTheme {
        PrezelTopAppBar(
            title = {
                Text(
                    text = "제목",
                    style = PrezelTextStyles.Body2Bold.toTextStyle(),
                )
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ThemePreview
@Composable
private fun PrezelTopAppBarWithLeadingPreview() {
    PrezelTheme {
        PrezelTopAppBar(
            title = {
                Text(
                    text = "제목",
                    style = PrezelTextStyles.Body2Bold.toTextStyle(),
                )
            },
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(PrezelIcons.Blank),
                        contentDescription = "뒤로가기",
                        tint = PrezelTheme.colors.iconRegular,
                    )
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ThemePreview
@Composable
private fun PrezelTopAppBarWithAllIconsPreview() {
    PrezelTheme {
        PrezelTopAppBar(
            title = {
                Text(
                    text = "제목",
                    style = PrezelTextStyles.Body2Bold.toTextStyle(),
                )
            },
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(PrezelIcons.Blank),
                        contentDescription = "뒤로가기",
                        tint = PrezelTheme.colors.iconRegular,
                    )
                }
            },
            trailingIcons = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(PrezelIcons.Blank),
                        contentDescription = "검색",
                        tint = PrezelTheme.colors.iconRegular,
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(PrezelIcons.Blank),
                        contentDescription = "더보기",
                        tint = PrezelTheme.colors.iconRegular,
                    )
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ThemePreview
@Composable
private fun PrezelTopAppBarScrollTestPreview() {
    PrezelTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                PrezelTopAppBar(
                    title = { Text("제목") },
                    leadingIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(PrezelIcons.Blank),
                                contentDescription = "뒤로가기",
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
            ) {
                items(30) { index ->
                    Text(
                        text = "Item $index",
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    }
}
